package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    //Can be placed in QuizQuestionId to implement various formats
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { questionValidating.id != QuizQuestionId.NONE && ! questionValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = questionValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
            field = "id",
            violationCode = "badFormat",
            description = "value $encodedId must contain only letters and numbers"
        )
        )
    }
}
