package dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.stubValidationBadAnswerOptions(title: String) = worker {
    this.title = title
    this.description = """
        Validation error case for answer options
    """.trimIndent()

    on { stubCase == QuizStubs.BAD_ANSWER_OPTIONS && state == QuizState.RUNNING }
    handle {
        fail(
            QuizError(
                group = "validation",
                code = "validation-answer",
                field = "answer options",
                message = "Wrong answer options field"
            )
        )
    }
}
