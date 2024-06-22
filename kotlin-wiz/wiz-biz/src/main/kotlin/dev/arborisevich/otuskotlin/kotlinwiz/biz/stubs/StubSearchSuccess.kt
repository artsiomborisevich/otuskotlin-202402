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

fun ICorChainDsl<QuizContext>.stubSearchSuccess(title: String, corSettings: QuizCoreSettings) = worker {
    this.title = title
    this.description = """
        Success case for question
    """.trimIndent()
    on { stubCase == QuizStubs.SUCCESS && state == QuizState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")

    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = QuizState.FINISHING
            questionsResponse.addAll(QuizQuestionStub.prepareSearchBeginnerList(
                questionFilterRequest.searchString,
                QuizQuestionLevel.BEGINNER)
            )
        }
    }
}
