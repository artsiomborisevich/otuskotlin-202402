package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateAnswerOptionsNotEmpty(title: String) = worker {
    this.title = title
    on { questionValidating.answerOptions.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "answerOptions",
            violationCode = "empty option",
            description = "field value must not be empty"
        )
        )
    }
}
