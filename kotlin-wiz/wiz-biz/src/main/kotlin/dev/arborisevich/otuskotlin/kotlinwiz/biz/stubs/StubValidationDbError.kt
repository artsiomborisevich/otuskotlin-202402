package dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
       Database error case
    """.trimIndent()
    on { stubCase == QuizStubs.DB_ERROR && state == QuizState.RUNNING }
    handle {
        fail(
            QuizError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
