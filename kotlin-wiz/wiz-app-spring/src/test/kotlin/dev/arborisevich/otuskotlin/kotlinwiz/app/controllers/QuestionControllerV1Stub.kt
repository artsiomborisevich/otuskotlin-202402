package dev.arborisevich.otuskotlin.kotlinwiz.app.controllers

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportCreate
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportDelete
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportRead
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportSearch
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportUpdate
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.app.config.QuestionConfig
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import kotlin.test.Test

@WebFluxTest(QuestionControllerV1::class, QuestionConfig::class)
internal class QuestionControllerV1Stub {

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun createQuestion() = testStubAd(
        "/v1/question/create",
        QuestionCreateRequest(),
        QuizContext(questionResponse = QuizQuestionStub.get(), state = QuizState.FINISHING)
            .toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readQuestion() = testStubAd(
        "/v1/question/read",
        QuestionReadRequest(),
        QuizContext(questionResponse = QuizQuestionStub.get(), state = QuizState.FINISHING)
            .toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateQuestion() = testStubAd(
        "/v1/question/update",
        QuestionUpdateRequest(),
        QuizContext(questionResponse = QuizQuestionStub.get(), state = QuizState.FINISHING)
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteQuestion() = testStubAd(
        "/v1/question/delete",
        QuestionDeleteRequest(),
        QuizContext(questionResponse = QuizQuestionStub.get(), state = QuizState.FINISHING)
            .toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchQuestion() = testStubAd(
        "/v1/question/search",
        QuestionSearchRequest(),
        QuizContext(questionsResponse = QuizQuestionStub.prepareSearchBeginnerList("What", QuizQuestionLevel.BEGINNER)
            .toMutableList(), state = QuizState.FINISHING)
            .toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubAd(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }

}