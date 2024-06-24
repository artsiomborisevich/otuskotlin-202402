package dev.arborisevich.otuskotlin.kotlinwiz.biz.general

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.chain

fun ICorChainDsl<QuizContext>.operation(
    title: String,
    command: QuizCommand,
    block: ICorChainDsl<QuizContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == QuizState.RUNNING }
}
