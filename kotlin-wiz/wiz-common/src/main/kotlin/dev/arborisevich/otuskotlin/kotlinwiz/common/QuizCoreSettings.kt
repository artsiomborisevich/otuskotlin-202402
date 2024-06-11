package dev.arborisevich.otuskotlin.kotlinwiz.common

import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.QuizLoggerProvider

data class QuizCoreSettings(
    val loggerProvider: QuizLoggerProvider = QuizLoggerProvider(),
) {
    companion object {
        val NONE = QuizCoreSettings()
    }
}
