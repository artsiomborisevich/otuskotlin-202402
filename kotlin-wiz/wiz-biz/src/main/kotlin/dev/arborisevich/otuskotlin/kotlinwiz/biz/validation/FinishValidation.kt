package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.finishQuestionValidation(title: String) = worker {
    this.title = title
    on { state == QuizState.RUNNING }
    handle {
        questionValidated = questionValidating
    }
}

fun ICorChainDsl<QuizContext>.finishQuestionFilterValidation(title: String) = worker {
    this.title = title
    on { state == QuizState.RUNNING }
    handle {
        questionFilterValidated = questionFilterValidating
    }
}
