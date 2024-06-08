package dev.arborisevich.otuskotlin.kotlinwiz.api.log1.mapper

import dev.arborisevich.otuskotlin.kotlinwiz.api.log1.models.CommonLogModel
import dev.arborisevich.otuskotlin.kotlinwiz.api.log1.models.ErrorLogModel
import dev.arborisevich.otuskotlin.kotlinwiz.api.log1.models.QuestionFilterLog
import dev.arborisevich.otuskotlin.kotlinwiz.api.log1.models.QuestionLog
import dev.arborisevich.otuskotlin.kotlinwiz.api.log1.models.QuizQuestionLogModel
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionFilter
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizRequestId
import kotlinx.datetime.Clock

fun QuizContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-marketplace",
    question = toQuizQuestionLog(),
    errors = errors.map { it.toLog() },
)

private fun QuizContext.toQuizQuestionLog(): QuizQuestionLogModel? {
    val questionNone = QuizQuestion()
    return QuizQuestionLogModel(
        requestId = requestId.takeIf { it != QuizRequestId.NONE }?.asString(),
        requestQuestion = questionNone.takeIf { it != questionNone }?.toLog(),
        responseQuestion = questionResponse.takeIf { it != questionNone }?.toLog(),
        responseQuestions = questionsResponse.takeIf { it.isNotEmpty() }?.filter { it != questionNone }?.map { it.toLog() },
        requestFilter = questionFilterRequest.takeIf { it != QuizQuestionFilter() }?.toLog(),
    ).takeIf { it != QuizQuestionLogModel() }
}

private fun QuizQuestionFilter.toLog() = QuestionFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
)

private fun QuizError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun QuizQuestion.toLog() = QuestionLog(
    id = id.takeIf { it != QuizQuestionId.NONE }?.asString(),
    text = text.takeIf { it.isNotBlank() },
    answer = answer.takeIf { it.isNotBlank() },
    explanation = explanation.takeIf { it.isNotBlank() },
    level = level.takeIf { it != QuizQuestionLevel.NONE }?.name,
    answerOptions = answerOptions.takeIf { it.isNotEmpty() }?.map { it.answerText }?.toList(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
