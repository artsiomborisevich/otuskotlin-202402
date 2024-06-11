package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionResponseObject
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.client.Client
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.beValidId
import dev.arborisevich.otuskotlin.kotlinwiz.test.action.v1.debug
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe

suspend fun Client.readQuestion(id: String?): QuestionResponseObject = readQuestion(id) {
    it should haveSuccessResult
    it.question shouldNotBe null
    it.question!!
}

suspend fun <T> Client.readQuestion(id: String?, block: (QuestionReadResponse) -> T): T =
    withClue("readAdV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "questions/read",
            QuestionReadRequest(
                requestType = "read",
                debug = debug,
                question = QuestionReadObject(id = id)
            )
        ) as QuestionReadResponse

        response.asClue(block)
    }
