package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateTextHasContent(title: String) = worker {
    this.title = title
    this.description = """
      We check that we have some words in the title.
      We refuse to publish titles that contain only meaningless characters like %^&^${'$'}^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { questionValidating.text.isNotEmpty() && !questionValidating.text.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "text",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
