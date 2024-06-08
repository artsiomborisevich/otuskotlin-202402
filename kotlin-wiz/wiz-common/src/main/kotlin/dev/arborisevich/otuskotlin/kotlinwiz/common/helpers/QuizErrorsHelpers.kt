package dev.arborisevich.otuskotlin.kotlinwiz.common.helpers

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError

fun Throwable.asQuizError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = QuizError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
