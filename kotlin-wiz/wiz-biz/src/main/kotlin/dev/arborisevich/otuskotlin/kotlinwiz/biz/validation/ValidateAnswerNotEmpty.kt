package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateAnswerNotEmpty(title: String) = worker {
    this.title = title
    on { questionValidating.answer.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "answer",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
