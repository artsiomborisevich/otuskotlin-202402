package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Preparing data to respond to a client's request"
    on { workMode != QuizWorkMode.STUB }
    handle {
        questionResponse = questionRepoDone
        questionsResponse = questionsRepoDone
        state = when (val st = state) {
            QuizState.RUNNING -> QuizState.FINISHING
            else -> st
        }
    }
}
