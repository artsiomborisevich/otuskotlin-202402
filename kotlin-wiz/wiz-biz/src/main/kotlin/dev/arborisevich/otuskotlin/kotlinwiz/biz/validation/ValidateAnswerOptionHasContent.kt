package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateAnswerOptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { questionValidating.answerOptions.all { it.answerText.isNotEmpty() } &&
            questionValidating.answerOptions.any { !it.answerText.contains(regExp) }}
    handle {
        fail(
            errorValidation(
                field = "answerOptions",
                violationCode = "noContent in option",
                description = "field value must contain letters"
            )
        )
    }
}
