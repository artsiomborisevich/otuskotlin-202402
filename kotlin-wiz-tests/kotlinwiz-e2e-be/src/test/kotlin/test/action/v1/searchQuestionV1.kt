package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionResponseObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchFilter
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchResponse
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.client.Client
import dev.arborisevich.otuskotlin.kotlinwiz.test.action.v1.debug
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should

suspend fun Client.searchQuestion(search: QuestionSearchFilter): List<QuestionResponseObject> = searchQuestion(search) {
    it should haveSuccessResult
    it.questions ?: listOf()
}

suspend fun <T> Client.searchQuestion(search: QuestionSearchFilter, block: (QuestionSearchResponse) -> T): T =
    withClue("searchAdV1: $search") {
        val response = sendAndReceive(
            "questions/search",
            QuestionSearchRequest(
                requestType = "search",
                debug = debug,
                questionFilter = search,
            )
        ) as QuestionSearchResponse

        response.asClue(block)
    }
