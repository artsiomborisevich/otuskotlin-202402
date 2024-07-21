package dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateObject
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock


fun QuizQuestion.toTransportCreate() = QuestionCreateObject(
    text = text.takeIf { it.isNotBlank() },
    answer = answer.takeIf { it.isNotBlank() },
    explanation = explanation.takeIf { it.isNotBlank() },
    level = level.toTransportQuestion(),
    answerOptions = answerOptions.toTransportAnswerOption(),
)

fun QuizQuestion.toTransportRead() = QuestionReadObject(
    id = id.takeIf { it != QuizQuestionId.NONE }?.asString(),
)

fun QuizQuestion.toTransportUpdate() = QuestionUpdateObject(
    id = id.takeIf { it != QuizQuestionId.NONE }?.asString(),
    text = text.takeIf { it.isNotBlank() },
    answer = answer.takeIf { it.isNotBlank() },
    explanation = explanation.takeIf { it.isNotBlank() },
    level = level.toTransportQuestion(),
    answerOptions = answerOptions.toTransportAnswerOption(),
    lock = lock.takeIf { it != QuizQuestionLock.NONE }?.asString(),
)

fun QuizQuestion.toTransportDelete() = QuestionDeleteObject(
    id = id.takeIf { it != QuizQuestionId.NONE }?.asString(),
    lock = lock.takeIf { it != QuizQuestionLock.NONE }?.asString(),
)
