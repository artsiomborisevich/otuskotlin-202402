package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Preparing data for deletion from the database
    """.trimIndent()
    on { state == QuizState.RUNNING }
    handle {
        questionRepoPrepare = questionValidated.deepCopy()
    }
}
