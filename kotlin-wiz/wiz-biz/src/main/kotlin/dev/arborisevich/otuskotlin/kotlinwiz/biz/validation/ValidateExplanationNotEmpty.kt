package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateExplanationNotEmpty(title: String) = worker {
    this.title = title
    on { questionValidating.explanation.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "explanation",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
