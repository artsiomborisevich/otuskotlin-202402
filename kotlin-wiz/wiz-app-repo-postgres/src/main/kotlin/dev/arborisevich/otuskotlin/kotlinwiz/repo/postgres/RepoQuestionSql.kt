package dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres

import com.benasher44.uuid.Uuid
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.asQuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionFilterRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionIdRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErr
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseErr
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IDbQuestionResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IDbQuestionsResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorEmptyId
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorNotFound
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorRepoConcurrency
import dev.arborisevich.otuskotlin.kotlinwiz.repo.IRepoQuestionInitializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class RepoQuestionSql constructor(
    properties: SqlProperties,
    private val randomUuid: () -> String
) : IRepoQuestionInitializable {

    private val questionTable = QuestionTable("${properties.schema}.${properties.questionTable}")
    private val answerOptionTable = AnswerOptionTable("${properties.schema}.${properties.answerOptionTable}")

    constructor(properties: SqlProperties) : this(properties, { Uuid.randomUUID().toString() }) {
    }

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        questionTable.deleteAll()
    }

    override fun save(questions: Collection<QuizQuestion>): Collection<QuizQuestion> = questions.map { saveObj(it) }

    override suspend fun createQuestion(rq: DbQuestionRequest): IDbQuestionResponse = transactionWrapper {
        DbQuestionResponseOk(saveObj(rq.question))
    }

    private fun saveObj(question: QuizQuestion): QuizQuestion = transaction(conn) {
        val res = questionTable
            .insert {
                to(it, question, randomUuid)
            }.resultedValues?.map { questionTable.from(it) }

        val quizQuestion = res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")

        question.answerOptions.forEach { answerOption ->
            val quizAnswerOption = answerOptionTable.insert {
                to(it, quizQuestion.id, answerOption)
            }.resultedValues?.map { answerOptionTable.from(it) }?.first()
                ?: throw RuntimeException("BD error: insert statement returned empty result")

            quizQuestion.answerOptions + quizAnswerOption
        }


        return@transaction quizQuestion
    }

    override suspend fun readQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse = transactionWrapper { read(rq.id) }

    private fun read(id: QuizQuestionId): IDbQuestionResponse {
        val complexJoin = Join(
            questionTable, answerOptionTable,
            onColumn = questionTable.id, otherColumn = answerOptionTable.questionId,
            joinType = JoinType.INNER
        )

        val resultRows = complexJoin
            .selectAll()
            .where { questionTable.id eq id.asString() }
            .toList()

        if (resultRows.isEmpty()) {
            return errorNotFound(id)
        }

        val answerOptions = resultRows.map { answerOptionTable.from(it) }
        val result: QuizQuestion = questionTable.from(resultRows.first())
        val questionWithOptions = result.copy(answerOptions = answerOptions)

        return DbQuestionResponseOk(questionWithOptions)
    }

    override suspend fun updateQuestion(rq: DbQuestionRequest): IDbQuestionResponse = update(
        rq.question.id,
        rq.question.lock
    ) {
        questionTable.update({ questionTable.id eq rq.question.id.asString() }) {
            to(it, rq.question.copy(lock = QuizQuestionLock(randomUuid())), randomUuid)
        }

        read(rq.question.id)
    }

    private suspend fun update(
        id: QuizQuestionId,
        lock: QuizQuestionLock,
        block: (QuizQuestion) -> IDbQuestionResponse
    ): IDbQuestionResponse =
        transactionWrapper {
            if (id == QuizQuestionId.NONE) return@transactionWrapper errorEmptyId

            val complexJoin = Join(
                questionTable, answerOptionTable,
                onColumn = questionTable.id, otherColumn = answerOptionTable.questionId,
                joinType = JoinType.INNER
            )

            val resRows = complexJoin.selectAll().where { questionTable.id eq id.asString() }.toList()

            val result = resRows.singleOrNull()?.let { questionTable.from(it) }
            val answerOptions = resRows.map { answerOptionTable.from(it) }
            val current = result?.copy(answerOptions = answerOptions)

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }

    override suspend fun deleteQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse = update(rq.id, rq.lock) {
        questionTable.deleteWhere { id eq rq.id.asString() }
        DbQuestionResponseOk(it)
    }

    override suspend fun searchQuestion(rq: DbQuestionFilterRequest): IDbQuestionsResponse =
        transactionWrapper({
            val res = questionTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.textFilter.isNotBlank()) {
                        add(questionTable.text like "%${rq.textFilter}%" or (questionTable.answer like "%${rq.textFilter}%"))

                    }
                }.reduce { a, b -> a and b }
            }

            val questions = res.map { row ->
                questionTable.from(row)
            }

            val result = questions.map { question ->
                val answerOptions = answerOptionTable
                    .selectAll()
                    .where { answerOptionTable.questionId eq question.id.asString() }
                    .map { answerOptionTable.from(it) }

                question.copy(answerOptions = answerOptions)
            }

            DbQuestionsResponseOk(data = result)
        }, {
            DbQuestionsResponseErr(it.asQuizError())
        })


    private suspend inline fun <T> transactionWrapper(
        crossinline block: () -> T,
        crossinline handle: (Exception) -> T
    ): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbQuestionResponse): IDbQuestionResponse =
        transactionWrapper(block) { DbQuestionResponseErr(it.asQuizError()) }

}
