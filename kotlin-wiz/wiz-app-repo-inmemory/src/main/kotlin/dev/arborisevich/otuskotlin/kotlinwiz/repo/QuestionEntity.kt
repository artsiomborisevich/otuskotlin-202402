package dev.arborisevich.otuskotlin.kotlinwiz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.*

data class QuestionEntity(
    val id: String? = null,
    val text: String? = null,
    val answerOptions: List<QuizAnswerOption>? = null,
    val answer: String? = null,
    val explanation: String? = null,
    val level: String? = null,

    val lock: String? = null,
) {
    constructor(model: QuizQuestion): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        text = model.text.takeIf { it.isNotBlank() },
        answer = model.answer.takeIf { it.isNotBlank() },
        explanation = model.explanation.takeIf { it.isNotBlank() },
        level = model.level.takeIf { it != QuizQuestionLevel.NONE }?.name,
        answerOptions = model.answerOptions.takeIf { it.isNotEmpty() }?.toList(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = QuizQuestion(
        id = id?.let { QuizQuestionId(it) }?: QuizQuestionId.NONE,
        text = text?: "",
        answer = answer?: "",
        explanation = explanation?: "",
        level = level?.let { QuizQuestionLevel.valueOf(it) }?: QuizQuestionLevel.NONE,
        answerOptions =  answerOptions ?: emptyList(),
        lock = lock?.let { QuizQuestionLock(it) } ?: QuizQuestionLock.NONE,
    )
}
