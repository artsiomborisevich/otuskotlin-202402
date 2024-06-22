package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand

abstract class BaseBizValidationTest {
    protected abstract val command: QuizCommand
    private val settings by lazy { QuizCoreSettings() }
    protected val processor by lazy { QuizQuestionProcessor(settings) }
}
