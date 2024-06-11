package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionResponseObject
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.client.Client
import dev.arborisevich.otuskotlin.kotlinwiz.test.action.v1.debug
import dev.arborisevich.otuskotlin.kotlinwiz.test.action.v1.someCreateQuestion
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

suspend fun Client.createQuestion(question: QuestionCreateObject = someCreateQuestion): QuestionResponseObject =
    createQuestion(question) {
        it should haveSuccessResult
        it.question shouldNotBe null
        it.question?.apply {
            text shouldBe question.text
            answer shouldBe question.answer
            explanation shouldBe question.explanation
            level shouldBe question.level
        }
        it.question!!
    }

suspend fun <T> Client.createQuestion(question: QuestionCreateObject = someCreateQuestion, block: (QuestionCreateResponse) -> T): T =
    withClue("createQuestionV1: $question") {
        val response = sendAndReceive(
            "questions/create", QuestionCreateRequest(
                requestType = "create",
                debug = debug,
                question = question
            )
        ) as QuestionCreateResponse

        response.asClue(block)
    }
