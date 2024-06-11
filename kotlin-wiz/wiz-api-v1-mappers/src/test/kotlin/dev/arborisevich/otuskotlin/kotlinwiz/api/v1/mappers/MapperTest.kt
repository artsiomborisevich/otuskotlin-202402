package dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateResponse
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
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizRequestId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizUserId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import org.junit.Test
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromTransportCreateReq() {
        //Given
        val req = QuestionCreateRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS,
            ),
            question = QuestionCreateObject(
                text = "text",
                answer = "answer",
                answerOptions = listOf(
                    AnswerOption("1","answerOption1"),
                    AnswerOption("2","answerOption2")
                    ),
                explanation = "explanation",
                level = QuestionLevel.ADVANCED,
            ),
        )

        val expectedAnswerOptions = listOf(
            QuizAnswerOption("1", "answerOption1"),
            QuizAnswerOption("2", "answerOption2")
        )

        //When
        val context = QuizContext()
        context.fromTransport(req)

        //Then
        assertEquals(QuizStubs.SUCCESS, context.stubCase)
        assertEquals(QuizWorkMode.STUB, context.workMode)
        assertEquals("text", context.questionRequest.text)
        assertEquals(expectedAnswerOptions, context.questionRequest.answerOptions)
        assertEquals("answer", context.questionRequest.answer)
        assertEquals("explanation", context.questionRequest.explanation)
        assertEquals(QuizQuestionLevel.ADVANCED, context.questionRequest.level)
    }

    @Test
    fun fromTransportReadReq() {
        //Given
        val req = QuestionReadRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS,
            ),
            question = QuestionReadObject(
               id = "id",
               userId = "userId",
            ),
        )

        //When
        val context = QuizContext()
        context.fromTransport(req)

        //Then
        assertEquals(QuizStubs.SUCCESS, context.stubCase)
        assertEquals(QuizWorkMode.STUB, context.workMode)
        assertEquals(QuizQuestionId("id"), context.questionRequest.id)
        assertEquals(QuizUserId("userId"), context.questionRequest.userId)
    }

    @Test
    fun fromTransportUpdateReq() {
        //Given
        val req = QuestionUpdateRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS,
            ),
            question = QuestionUpdateObject(
                id = "id",
                text = "text",
                answer = "answer",
                answerOptions = listOf(
                    AnswerOption("1","answerOption1"),
                    AnswerOption("2","answerOption2")
                ),
                explanation = "explanation",
                level = QuestionLevel.ADVANCED,
            ),
        )

        //When
        val context = QuizContext()
        context.fromTransport(req)

        val expectedAnswerOptions = listOf(
            QuizAnswerOption("1", "answerOption1"),
            QuizAnswerOption("2", "answerOption2")
        )

        //Then
        assertEquals(QuizStubs.SUCCESS, context.stubCase)
        assertEquals(QuizWorkMode.STUB, context.workMode)
        assertEquals("text", context.questionRequest.text)
        assertEquals(QuizQuestionId("id"), context.questionRequest.id)
        assertEquals(expectedAnswerOptions, context.questionRequest.answerOptions)
        assertEquals("answer", context.questionRequest.answer)
        assertEquals("explanation", context.questionRequest.explanation)
        assertEquals(QuizQuestionLevel.ADVANCED, context.questionRequest.level)
    }

    @Test
    fun fromTransportDeleteReq() {
        //Given
        val req = QuestionDeleteRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS,
            ),
            question = QuestionDeleteObject(
                id = "id",
                lock = "lock",
            ),
        )

        //When
        val context = QuizContext()
        context.fromTransport(req)

        //Then
        assertEquals(QuizStubs.SUCCESS, context.stubCase)
        assertEquals(QuizWorkMode.STUB, context.workMode)
        assertEquals(QuizQuestionId("id"), context.questionRequest.id)
        assertEquals(QuizQuestionLock("lock"), context.questionRequest.lock)
    }

    @Test
    fun fromTransportSearchReq() {
        //Given
        val req = QuestionSearchRequest(
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS,
            ),
            questionFilter = QuestionSearchFilter(
                searchString = "searchString",
            ),
        )

        //When
        val context = QuizContext()
        context.fromTransport(req)

        //Then
        assertEquals(QuizStubs.SUCCESS, context.stubCase)
        assertEquals(QuizWorkMode.STUB, context.workMode)
        assertEquals("searchString", context.questionFilterRequest.searchString)
    }

    @Test
    fun toTransport() {
        //Given
        val context = QuizContext(
            requestId =  QuizRequestId("1234"),
            command =  QuizCommand.CREATE,
            questionResponse = QuizQuestion(
                text = "text",
                answer = "answer",
                answerOptions = listOf(
                    QuizAnswerOption("1","answerOption1"),
                    QuizAnswerOption("2","answerOption2")
                ),
                explanation = "explanation",
                level = QuizQuestionLevel.ADVANCED,
            ),
            errors = mutableListOf(
                QuizError(
                    code = "err",
                    group = "request",
                    field = "answer",
                    message = "wrong answer",
                )
            ),
            state = QuizState.RUNNING,
        )

        val expectedAnswerOptions = listOf(
            AnswerOption("1", "answerOption1"),
            AnswerOption("2", "answerOption2")
        )

        //When
        val res = context.toTransportQuestion() as QuestionCreateResponse

        //Then
        assertEquals("text", res.question?.text)
        assertEquals("answer", res.question?.answer)
        assertEquals("explanation", res.question?.explanation)
        assertEquals(QuestionLevel.ADVANCED, res.question?.level)
        assertEquals(expectedAnswerOptions, res.question?.answerOptions)
        assertEquals(1, res.errors?.size)
        assertEquals("err", res.errors?.firstOrNull()?.code)
        assertEquals("request", res.errors?.firstOrNull()?.group)
        assertEquals("answer", res.errors?.firstOrNull()?.field)
        assertEquals("wrong answer", res.errors?.firstOrNull()?.message)
    }
}
