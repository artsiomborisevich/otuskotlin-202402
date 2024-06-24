package dev.arborisevich.otuskotlin.kotlinwiz.biz.general

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        This handler sets the starting processing status. Runs only if the status is not specified.
    """.trimIndent()
    on { state == QuizState.NONE }
    handle { state = QuizState.RUNNING }
}
