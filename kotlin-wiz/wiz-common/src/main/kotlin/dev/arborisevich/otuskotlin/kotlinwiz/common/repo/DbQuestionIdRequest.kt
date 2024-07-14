package dev.arborisevich.otuskotlin.kotlinwiz.common.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock

data class DbQuestionIdRequest(
    val id: QuizQuestionId,
    val lock: QuizQuestionLock = QuizQuestionLock.NONE,
) {

    constructor(question: QuizQuestion): this(question.id, question.lock)
}
