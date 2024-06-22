package dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.chain

fun ICorChainDsl<QuizContext>.stubs(title: String, block: ICorChainDsl<QuizContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == QuizWorkMode.STUB && state == QuizState.RUNNING }
}
