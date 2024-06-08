package dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDebug
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugMode
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugStubs
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchFilter
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionFilter
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizUserId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import ru.otus.otuskotlin.marketplace.mappers.v1.exceptions.UnknownRequestClass

fun QuizContext.fromTransport(request: IRequest) = when (request) {
    is QuestionCreateRequest -> fromTransport(request)
    is QuestionReadRequest -> fromTransport(request)
    is QuestionUpdateRequest -> fromTransport(request)
    is QuestionDeleteRequest -> fromTransport(request)
    is QuestionSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun QuestionDebug?.transportToWorkMode(): QuizWorkMode = when (this?.mode) {
    QuestionRequestDebugMode.PROD -> QuizWorkMode.PROD
    QuestionRequestDebugMode.TEST -> QuizWorkMode.TEST
    QuestionRequestDebugMode.STUB -> QuizWorkMode.STUB
    null -> QuizWorkMode.PROD
}

private fun QuestionDebug?.transportToStubCase(): QuizStubs = when (this?.stub) {
    QuestionRequestDebugStubs.SUCCESS -> QuizStubs.SUCCESS
    QuestionRequestDebugStubs.NOT_FOUND -> QuizStubs.NOT_FOUND
    QuestionRequestDebugStubs.BAD_ID -> QuizStubs.BAD_ID
    QuestionRequestDebugStubs.BAD_TEXT -> QuizStubs.BAD_TEXT
    QuestionRequestDebugStubs.BAD_ANSWER -> QuizStubs.BAD_ANSWER
    QuestionRequestDebugStubs.BAD_CHOICES -> QuizStubs.BAD_CHOICES
    QuestionRequestDebugStubs.BAD_EXPLANATION -> QuizStubs.BAD_EXPLANATION
    QuestionRequestDebugStubs.CANNOT_DELETE -> QuizStubs.CANNOT_DELETE
    QuestionRequestDebugStubs.BAD_SEARCH_STRING -> QuizStubs.BAD_SEARCH_STRING
    null -> QuizStubs.NONE
}

fun QuizContext.fromTransport(request: QuestionCreateRequest) {
    command = QuizCommand.CREATE
    questionRequest = request.question?.toInternal() ?: QuizQuestion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun QuizContext.fromTransport(request: QuestionReadRequest) {
    command = QuizCommand.READ
    questionRequest = request.question.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun QuestionReadObject?.toInternal(): QuizQuestion = if (this != null) {
    QuizQuestion(
        id = id.toQuestionId(),
        userId = userId.toUserId()
    )
} else {
    QuizQuestion()
}

private fun String?.toQuestionId() = this?.let { QuizQuestionId(it) } ?: QuizQuestionId.NONE
private fun String?.toUserId() = this?.let { QuizUserId(it) } ?: QuizUserId.NONE

fun QuizContext.fromTransport(request: QuestionUpdateRequest) {
    command = QuizCommand.UPDATE
    questionRequest = request.question?.toInternal() ?: QuizQuestion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun QuizContext.fromTransport(request: QuestionDeleteRequest) {
    command = QuizCommand.DELETE
    questionRequest = request.question.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun QuestionDeleteObject?.toInternal(): QuizQuestion = if (this != null) {
    QuizQuestion(
        id = id.toQuestionId(),
        lock = lock.toQuestionLock(),
    )
} else {
    QuizQuestion()
}

private fun String?.toQuestionLock() = this?.let { QuizQuestionLock(it) } ?: QuizQuestionLock.NONE

fun QuizContext.fromTransport(request: QuestionSearchRequest) {
    command = QuizCommand.SEARCH
    questionFilterRequest = request.questionFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun QuestionSearchFilter?.toInternal(): QuizQuestionFilter = QuizQuestionFilter(
    searchString = this?.searchString ?: ""
)

private fun QuestionCreateObject.toInternal(): QuizQuestion = QuizQuestion(
    text = this.text ?: "",
    answerOptions = this.answerOptions.fromTransport(),
    answer = this.answer ?: "",
    explanation = this.explanation ?: "",
    level = this.level.fromTransport(),
)

private fun QuestionUpdateObject.toInternal(): QuizQuestion = QuizQuestion(
    id = this.id.toQuestionId(),
    text = this.text ?: "",
    answerOptions = this.answerOptions.fromTransport(),
    answer = this.answer ?: "",
    explanation = this.explanation ?: "",
    level = this.level.fromTransport(),
    lock = lock.toQuestionLock(),
)

private fun List<AnswerOption>?.fromTransport(): List<QuizAnswerOption> {
    if (this == null) return emptyList()

    return map { answerOption ->
        QuizAnswerOption(
            id = answerOption.id ?: "",
            answerText = answerOption.text ?: ""
        )
    }
}

private fun QuestionLevel?.fromTransport(): QuizQuestionLevel = when (this) {
    QuestionLevel.BEGINNER -> QuizQuestionLevel.BEGINNER
    QuestionLevel.ADVANCED -> QuizQuestionLevel.ADVANCED
    QuestionLevel.EXPERT -> QuizQuestionLevel.EXPERT
    null -> QuizQuestionLevel.NONE
}
