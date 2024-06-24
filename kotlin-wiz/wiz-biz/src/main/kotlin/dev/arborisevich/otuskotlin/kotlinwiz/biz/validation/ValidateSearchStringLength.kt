package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorValidation
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.chain
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    this.description = """
          Validation of search string length in search filters. Valid values:
        - null - do not search by string
        - 3-100 - permissible length
        - more than 100 - line too long
    """.trimIndent()
    on { state == QuizState.RUNNING }
    worker("Trim empty characters") {
        questionFilterValidating.searchString = questionFilterValidating.searchString.trim() }
    worker {
        this.title = "Checking case length for 0-2 characters"
        this.description = this.title
        on { state == QuizState.RUNNING && questionFilterValidating.searchString.length in (1..2) }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "Search string must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Checking a case length of more than 100 characters"
        this.description = this.title
        on { state == QuizState.RUNNING && questionFilterValidating.searchString.length > 100 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "Search string must be no more than 100 symbols long"
                )
            )
        }
    }
}
