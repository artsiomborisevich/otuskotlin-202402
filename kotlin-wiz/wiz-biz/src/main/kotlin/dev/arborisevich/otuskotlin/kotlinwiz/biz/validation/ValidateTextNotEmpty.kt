package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateTextNotEmpty(title: String) = worker {
    this.title = title
    on { questionValidating.text.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "text",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
