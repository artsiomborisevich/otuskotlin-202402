package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionResponseObject
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.client.Client
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.beValidId
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.beValidLock
import dev.arborisevich.otuskotlin.kotlinwiz.test.action.v1.debug
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

suspend fun Client.deleteQuestion(question: QuestionResponseObject) {
    val id = question.id
    val lock = question.lock
    withClue("deleteAdV1: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "questions/delete",
            QuestionDeleteRequest(
                debug = debug,
                question = QuestionDeleteObject(id = id, lock = lock)
            )
        ) as QuestionDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.question shouldBe question
//            response.ad?.id shouldBe id
        }
    }
}
