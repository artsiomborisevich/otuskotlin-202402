package dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.LogLevel
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub


fun ICorChainDsl<QuizContext>.stubUpdateSuccess(title: String, corSettings: QuizCoreSettings) = worker {
    this.title = title
    this.description = """
        Success case for changing a question
    """.trimIndent()
    on { stubCase == QuizStubs.SUCCESS && state == QuizState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubUpdateSuccess")

    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = QuizState.FINISHING
            val stub = QuizQuestionStub.prepareResult {
                questionRequest.text.takeIf { it.isNotBlank() }?.also { this.text = it }
                questionRequest.answer.takeIf { it.isNotBlank() }?.also { this.answer = it }
                questionRequest.explanation.takeIf { it.isNotBlank() }?.also { this.explanation = it }
                questionRequest.level.takeIf { it != QuizQuestionLevel.NONE }?.also { this.level = it }
                questionRequest.answerOptions.takeIf { it.isNotEmpty() }?.also { this.answerOptions = it }
            }
            questionResponse = stub
        }
    }
}
