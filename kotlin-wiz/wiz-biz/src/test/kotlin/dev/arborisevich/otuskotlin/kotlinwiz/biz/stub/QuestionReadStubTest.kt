package dev.arborisevich.otuskotlin.kotlinwiz.biz.stub

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class QuestionReadStubTest {

    private val processor = QuizQuestionProcessor()
    val id = QuizQuestionId("666")

    @Test
    fun read() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.READ,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.SUCCESS,
            questionRequest = QuizQuestion(
                id = id,
            ),
        )

        processor.exec(ctx)

        with (QuizQuestionStub.get()) {
            assertEquals(id, ctx.questionResponse.id)
            assertEquals(text, ctx.questionResponse.text)
            assertEquals(answer, ctx.questionResponse.answer)
            assertEquals(answerOptions, ctx.questionResponse.answerOptions)
            assertEquals(explanation, ctx.questionResponse.explanation)
            assertEquals(level, ctx.questionResponse.level)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.READ,
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
    fun databaseError() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.READ,
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
            command = QuizCommand.READ,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.BAD_TEXT,
            questionRequest = QuizQuestion(
                id = id,
            ),
        )

        processor.exec(ctx)

        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
