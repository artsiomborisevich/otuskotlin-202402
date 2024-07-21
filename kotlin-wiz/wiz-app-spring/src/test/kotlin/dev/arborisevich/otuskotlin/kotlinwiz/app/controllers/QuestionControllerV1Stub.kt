package dev.arborisevich.otuskotlin.kotlinwiz.app.controllers

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportCreate
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportDelete
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportRead
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportSearch
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportUpdate
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDebug
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugMode
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugStubs
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchFilter
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateObject
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
    fun createQuestion() {
        val request = QuestionCreateRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            question = QuestionCreateObject(
                text = "What is the difference between var and val in Kotlin?",
                answer = "var is mutable, val is immutable",
                answerOptions = listOf(
                    AnswerOption("1", "var is mutable, val is immutable"),
                    AnswerOption("2", "val is mutable, var is immutable"),
                    AnswerOption("3", "Both var and val are mutable"),
                    AnswerOption("4", "Both var and val are immutable"),
                ),
                explanation = "In Kotlin, 'var' is used to declare a mutable variable, while 'val' is used to declare an immutable variable.",
                level = QuestionLevel.BEGINNER,
            )
        )

        testStubAd(
            "/v1/questions/create",
            request,
            QuizContext(questionResponse = QuizQuestionStub.get(), state = QuizState.FINISHING)
                .toTransportCreate().copy(responseType = "create")
        )
    }

    @Test
    fun readQuestion() {
        val request = QuestionReadRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            question = QuestionReadObject("666")
        )

        testStubAd(
            "/v1/questions/read",
            request,
            QuizContext(questionResponse = QuizQuestionStub.get(), state = QuizState.FINISHING)
                .toTransportRead().copy(responseType = "read")
        )
    }

    @Test
    fun updateQuestion() {
        val request = QuestionUpdateRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            question = QuestionUpdateObject(
                text = "What is the difference between var and val in Kotlin?",
                answer = "var is mutable, val is immutable",
                answerOptions = listOf(
                    AnswerOption("1", "var is mutable, val is immutable"),
                    AnswerOption("2", "val is mutable, var is immutable"),
                    AnswerOption("3", "Both var and val are mutable"),
                    AnswerOption("4", "Both var and val are immutable"),
                ),
                explanation = "In Kotlin, 'var' is used to declare a mutable variable, while 'val' is used to declare an immutable variable.",
                level = QuestionLevel.BEGINNER,
            )
        )

        testStubAd(
            "/v1/questions/update",
            request,
            QuizContext(questionResponse = QuizQuestionStub.get(), state = QuizState.FINISHING)
                .toTransportUpdate().copy(responseType = "update")
        )
    }

    @Test
    fun deleteQuestion() {
        val request = QuestionDeleteRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            question = QuestionDeleteObject(
                id = "666",
                lock = "123"
            )
        )
        testStubAd(
            "/v1/questions/delete",
            request,
            QuizContext(questionResponse = QuizQuestionStub.get(), state = QuizState.FINISHING)
                .toTransportDelete().copy(responseType = "delete")
        )
    }

    @Test
    fun searchQuestion() {
        val request = QuestionSearchRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            questionFilter = QuestionSearchFilter(),
        )

        testStubAd(
            "/v1/questions/search",
            request,
            QuizContext(
                questionsResponse = QuizQuestionStub.prepareSearchBeginnerList("", QuizQuestionLevel.BEGINNER)
                    .toMutableList(), state = QuizState.FINISHING
            )
                .toTransportSearch().copy(responseType = "search")
        )
    }

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