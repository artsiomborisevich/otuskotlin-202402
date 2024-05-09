package ru.otus.otuskotlin.marketplace.mappers.v1

import QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.Error
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionPermissions
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionResponseObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.ResponseResult
import exceptions.UnknownQuizCommand
import models.QuizAnswerOption
import models.QuizCommand
import models.QuizError
import models.QuizQuestion
import models.QuizQuestionId
import models.QuizQuestionLevel
import models.QuizQuestionPermissionsClient
import models.QuizState
import models.QuizUserId

fun QuizContext.toTransportQuestion(): IResponse = when (val cmd = command) {
    QuizCommand.CREATE -> toTransportCreate()
    QuizCommand.READ -> toTransportRead()
    QuizCommand.UPDATE -> toTransportUpdate()
    QuizCommand.DELETE -> toTransportDelete()
    QuizCommand.SEARCH -> toTransportSearch()
    QuizCommand.NONE -> throw UnknownQuizCommand(cmd)
}

fun QuizContext.toTransportCreate() = QuestionCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    question = questionResponse.toTransportQuestion()
)

fun QuizContext.toTransportRead() = QuestionReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    question = questionResponse.toTransportQuestion()
)

fun QuizContext.toTransportUpdate() = QuestionUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    question = questionResponse.toTransportQuestion()
)

fun QuizContext.toTransportDelete() = QuestionDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    question = questionResponse.toTransportQuestion()
)

fun QuizContext.toTransportSearch() = QuestionSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    questions = questionsResponse.toTransportQuestion()
)

fun List<QuizQuestion>.toTransportQuestion(): List<QuestionResponseObject>? = this
    .map { it.toTransportQuestion() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun QuizQuestion.toTransportQuestion(): QuestionResponseObject = QuestionResponseObject(
    id = id.takeIf { it != QuizQuestionId.NONE }?.asString(),
    text = text.takeIf { it.isNotBlank() },
    answer = answer.takeIf { it.isNotBlank() },
    answerOptions = answerOptions.toTransportAnswerOption(),
    explanation = explanation.takeIf { it.isNotBlank() },
    userId = userId.takeIf { it != QuizUserId.NONE }?.asString(),
    level = level.toTransportQuestion(),
    permissions = permissionsClient.toTransportQuestion(),
)

private fun List<QuizAnswerOption>.toTransportAnswerOption(): List<AnswerOption>? {

    return map { quizAnswerOption ->
        AnswerOption(
            id = quizAnswerOption.id,
            text = quizAnswerOption.answerText
        )
    }
        .toList()
        .takeIf { it.isNotEmpty() }
}

private fun QuizQuestionLevel.toTransportQuestion(): QuestionLevel? = when (this) {
    QuizQuestionLevel.BEGINNER -> QuestionLevel.BEGINNER
    QuizQuestionLevel.ADVANCED -> QuestionLevel.ADVANCED
    QuizQuestionLevel.EXPERT -> QuestionLevel.EXPERT
    QuizQuestionLevel.NONE -> null
}

private fun Set<QuizQuestionPermissionsClient>.toTransportQuestion(): Set<QuestionPermissions>? = this
    .map { it.toTransportQuestion() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun QuizQuestionPermissionsClient.toTransportQuestion() = when (this) {
    QuizQuestionPermissionsClient.READ -> QuestionPermissions.READ
    QuizQuestionPermissionsClient.UPDATE -> QuestionPermissions.UPDATE
    QuizQuestionPermissionsClient.DELETE -> QuestionPermissions.DELETE
}

private fun List<QuizError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportQuestion() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun QuizError.toTransportQuestion() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun QuizState.toResult(): ResponseResult? = when (this) {
    QuizState.RUNNING -> ResponseResult.SUCCESS
    QuizState.FAILING -> ResponseResult.ERROR
    QuizState.FINISHING -> ResponseResult.SUCCESS
    QuizState.NONE -> null
}
