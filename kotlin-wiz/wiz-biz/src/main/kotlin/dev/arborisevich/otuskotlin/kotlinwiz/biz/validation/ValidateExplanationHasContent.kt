package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateExplanationHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { questionValidating.explanation.isNotEmpty() && !questionValidating.explanation.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "explanation",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
