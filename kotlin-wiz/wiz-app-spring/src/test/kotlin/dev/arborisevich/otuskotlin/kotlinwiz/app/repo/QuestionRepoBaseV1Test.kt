package dev.arborisevich.otuskotlin.kotlinwiz.app.repo

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportCreate
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportDelete
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportRead
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportSearch
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportUpdate
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDebug
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugMode
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchFilter
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import kotlin.test.Test

internal abstract class QuestionRepoBaseV1Test {

    protected abstract var webClient: WebTestClient
    private val debug = QuestionDebug(mode = QuestionRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createQuestion() = testRepoQuestion(
        "create",
        QuestionCreateRequest(
            question = QuizQuestionStub.get().toTransportCreate(),
            debug = debug,
        ),

        prepareCtx(QuizQuestionStub.prepareResult {
            id = QuizQuestionId(uuidNew)
            lock = QuizQuestionLock.NONE
        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readQuestion() = testRepoQuestion(
        "read",
        QuestionReadRequest(
            question = QuizQuestionStub.prepareResult {
                id = QuizQuestionId("d-666-01")
                text= " d-666-01"
                answer="answer  d-666-01"
            }.toTransportRead(),
            debug = debug,
        ),
        prepareCtx(QuizQuestionStub.prepareResult {
            id = QuizQuestionId("d-666-01")
            text= " d-666-01"
            answer="answer  d-666-01"
        })
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updateQuestion() = testRepoQuestion(
        "update",
        QuestionUpdateRequest(
            question = QuizQuestionStub.prepareResult {
                id = QuizQuestionId("d-666-01")
                text= "d-666-01"
                answer="answer  d-666-01"
            }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(QuizQuestionStub.prepareResult {
            id = QuizQuestionId("d-666-01")
            text= "d-666-01"
            answer="answer  d-666-01"
        })
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun deleteQuestion() = testRepoQuestion(
        "delete",
        QuestionDeleteRequest(
            question = QuizQuestionStub.prepareResult {
                id = QuizQuestionId("d-666-01")
                text= "d-666-01"
                answer="answer  d-666-01"
            }.toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(QuizQuestionStub.prepareResult {
            id = QuizQuestionId("d-666-01")
            text = " d-666-01"
            answer="answer  d-666-01"
        })
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchQuestion() = testRepoQuestion(
        "search",
        QuestionSearchRequest(
            questionFilter = QuestionSearchFilter(searchString = "Question"),
            debug = debug,
        ),
        QuizContext(
            state = QuizState.RUNNING,
            questionsResponse = QuizQuestionStub.prepareSearchBeginnerList("", QuizQuestionLevel.BEGINNER)
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )

    private fun prepareCtx(Question: QuizQuestion) = QuizContext(
        state = QuizState.RUNNING,
        questionResponse = Question.apply {
            //TODO
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoQuestion(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/questions/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is QuestionSearchResponse -> it.copy(questions = it.questions?.sortedBy { it.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
