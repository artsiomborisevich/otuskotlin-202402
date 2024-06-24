package dev.arborisevich.otuskotlin.kotlinwiz.biz.stub

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class QuestionUpdateStubTest {

    private val processor = QuizQuestionProcessor()
    val id = QuizQuestionId("666")
    val text = "text"
    val answer = "answer"
    val answerOptions = listOf(
        QuizAnswerOption("1","answerOption1"),
        QuizAnswerOption("2","answerOption2")
    )
    val explanation = "explanation"
    val level = QuizQuestionLevel.ADVANCED

    @Test
    fun create() = runTest {

        val ctx = QuizContext(
            command = QuizCommand.UPDATE,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.SUCCESS,
            questionRequest = QuizQuestion(
                id = id,
                text = text,
                answer = answer,
                answerOptions = answerOptions,
                explanation = explanation,
                level = level,
            ),
        )

        processor.exec(ctx)

        assertEquals(QuizQuestionStub.get().id, ctx.questionResponse.id)
        assertEquals(text, ctx.questionResponse.text)
        assertEquals(answer, ctx.questionResponse.answer)
        assertEquals(answerOptions, ctx.questionResponse.answerOptions)
        assertEquals(explanation, ctx.questionResponse.explanation)
        assertEquals(level, ctx.questionResponse.level)
    }

    @Test
    fun badId() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.UPDATE,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.BAD_ID,
            questionRequest = QuizQuestion(),
        )

        processor.exec(ctx)

        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badText() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.UPDATE,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.BAD_TEXT,
            questionRequest = QuizQuestion(
                id = id,
                text = "",
                answer = answer,
                answerOptions = answerOptions,
                explanation = explanation,
                level = level,
            ),
        )

        processor.exec(ctx)

        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("text", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badAnswer() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.UPDATE,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.BAD_ANSWER,
            questionRequest = QuizQuestion(
                id = id,
                text = text,
                answer = "",
                answerOptions = answerOptions,
                explanation = explanation,
                level = level,
            ),
        )

        processor.exec(ctx)

        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("answer", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.UPDATE,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.DB_ERROR,
            questionRequest = QuizQuestion(
                id = id,
            ),
        )

        processor.exec(ctx)
        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.UPDATE,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.BAD_SEARCH_STRING,
            questionRequest = QuizQuestion(
                id = id,
                text = text,
                answer = answer,
                answerOptions = answerOptions,
                explanation = explanation,
                level = level,
            ),
        )

        processor.exec(ctx)

        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
