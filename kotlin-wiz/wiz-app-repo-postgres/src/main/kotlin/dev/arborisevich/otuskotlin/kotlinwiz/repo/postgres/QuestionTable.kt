package dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class QuestionTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.QUESTION_ID)
    val text = text(SqlFields.QUESTION_TEXT)
    val level = levelEnumeration(SqlFields.LEVEL)
    val answer = text(SqlFields.ANSWER)
    val explanation = text(SqlFields.EXPLANATION)

    val lock = text(SqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = QuizQuestion(
        id = QuizQuestionId(res[id].toString()),
        text = res[text] ?: "",
        answer = res[answer] ?: "",
        explanation = res[explanation] ?: "",
        level = res[level],
        lock = QuizQuestionLock(res[lock]),
    )

    fun to(it: UpdateBuilder<*>, question: QuizQuestion, randomUuid: () -> String) {
        it[id] = question.id.takeIf { it != QuizQuestionId.NONE }?.asString() ?: randomUuid()
        it[text] = question.text
        it[answer] = question.answer
        it[explanation] = question.explanation
        it[level] = question.level
        it[lock] = question.lock.takeIf { it != QuizQuestionLock.NONE }?.asString() ?: randomUuid()
    }

}

