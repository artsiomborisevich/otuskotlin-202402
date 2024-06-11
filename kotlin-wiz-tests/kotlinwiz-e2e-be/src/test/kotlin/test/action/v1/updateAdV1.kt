package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionResponseObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.client.Client
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.beValidId
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.beValidLock
import dev.arborisevich.otuskotlin.kotlinwiz.test.action.v1.debug
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

suspend fun Client.updateQuestion(question: QuestionUpdateObject): QuestionResponseObject =
    updateQuestion(question) {
        it should haveSuccessResult
        it.question shouldNotBe null
        it.question?.apply {
            if (question.text != null)
                text shouldBe question.text
            if (question.answer != null)
                answer shouldBe question.answer
            if (question.explanation != null)
                explanation shouldBe question.explanation
            if (question.level != null)
                level shouldBe question.level
            if (question.answerOptions != null)
                answerOptions shouldBe question.answerOptions
        }
        it.question!!
    }

suspend fun <T> Client.updateQuestion(question: QuestionUpdateObject, block: (QuestionUpdateResponse) -> T): T {
    val id = question.id
    val lock = question.lock
    return withClue("updatedV1: $id, lock: $lock, set: $question") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "questions/update", QuestionUpdateRequest(
                debug = debug,
                question = question.copy(id = id, lock = lock)
            )
        ) as QuestionUpdateResponse

        response.asClue(block)
    }
}
