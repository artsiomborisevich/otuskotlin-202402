
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.fromTransport
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDebug
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugMode
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugStubs
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.ResponseResult
import dev.arborisevich.otuskotlin.kotlinwiz.app.common.IQuizAppSettings
import dev.arborisevich.otuskotlin.kotlinwiz.app.common.controllerHelper
import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = QuestionCreateRequest(
        question = QuestionCreateObject(
            text = "Question text",
            answer = "Question answer",
            answerOptions = listOf(AnswerOption("1","first option")),
            explanation = "Question explanation",
            level = QuestionLevel.BEGINNER,
        ),
        debug = QuestionDebug(mode = QuestionRequestDebugMode.STUB, stub = QuestionRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IQuizAppSettings = object : IQuizAppSettings {
        override val corSettings: QuizCoreSettings = QuizCoreSettings()
        override val processor: QuizQuestionProcessor = QuizQuestionProcessor(corSettings)
    }

    @Test
    fun ktorHelperTest() = runBlocking {
        val testApp = TestApplicationCall(request).apply { createQuestionKtor(appSettings) }
        val res = testApp.res as QuestionCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createQuestionSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T

        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createQuestionKtor(appSettings: IQuizAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<QuestionCreateRequest>()) },
            { toTransportQuestion() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    private suspend fun createQuestionSpring(request: QuestionCreateRequest): QuestionCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportQuestion() as QuestionCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )
}
