package dev.arborisevich.otuskotlin.kotlinwiz.common.models

import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.LogLevel

data class QuizError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
