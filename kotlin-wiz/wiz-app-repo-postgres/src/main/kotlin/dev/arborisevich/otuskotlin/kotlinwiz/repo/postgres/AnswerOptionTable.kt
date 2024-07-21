package dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class AnswerOptionTable(tableName: String) : Table(tableName) {
    val id = integer(SqlFields.ANSWER_OPTION_ID).autoIncrement()
    val optionId = text(SqlFields.OPTION_ID)
    val optionText = text(SqlFields.OPTION_TEXT)
    val questionId = text(SqlFields.QUESTION_ID).references(QuestionTable("").id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = QuizAnswerOption(
        id = res[optionId] ?: "",
        answerText = res[optionText] ?: "",
    )

    fun to(it: UpdateBuilder<*>, quizQuestionId: QuizQuestionId, answerOption: QuizAnswerOption) {
        it[questionId] = quizQuestionId.asString()
        it[optionId] = answerOption.id
        it[optionText] = answerOption.answerText
    }

}

