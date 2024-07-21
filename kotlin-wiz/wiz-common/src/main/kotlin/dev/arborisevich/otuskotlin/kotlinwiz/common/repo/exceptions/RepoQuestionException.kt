package dev.arborisevich.otuskotlin.kotlinwiz.common.repo.exceptions

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId

open class RepoQuestionException(
    @Suppress("unused")
    val questionId: QuizQuestionId,
    msg: String,
): RepoException(msg)
