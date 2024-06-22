package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.chain

fun ICorChainDsl<QuizContext>.validation(block: ICorChainDsl<QuizContext>.() -> Unit) = chain {
    block()
    title = "Validation"

    on { state == QuizState.RUNNING }
}
