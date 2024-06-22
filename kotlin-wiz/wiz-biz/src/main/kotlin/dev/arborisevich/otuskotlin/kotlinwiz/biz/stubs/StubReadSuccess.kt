package dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.LogLevel
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub

fun ICorChainDsl<QuizContext>.stubReadSuccess(title: String, corSettings: QuizCoreSettings) = worker {
    this.title = title
    this.description = """
        Success case for reading a question
    """.trimIndent()
    on { stubCase == QuizStubs.SUCCESS && state == QuizState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubReadSuccess")

    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = QuizState.FINISHING
            val stub = QuizQuestionStub.prepareResult {
                questionRequest.text.takeIf { it.isNotBlank() }?.also { this.text = it }
            }

            questionResponse = stub
        }
    }
}
