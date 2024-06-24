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

class QuestionDeleteStubTest {

    private val processor = QuizQuestionProcessor()
    val id = QuizQuestionId("666")

    @Test
    fun delete() = runTest {

        val ctx = QuizContext(
            command = QuizCommand.DELETE,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.SUCCESS,
            questionRequest = QuizQuestion(
                id = id,
            ),
        )

        processor.exec(ctx)

        val stub = QuizQuestionStub.get()

        assertEquals(stub.id, ctx.questionResponse.id)
        assertEquals(stub.text, ctx.questionResponse.text)
        assertEquals(stub.answer, ctx.questionResponse.answer)
        assertEquals(stub.answerOptions, ctx.questionResponse.answerOptions)
        assertEquals(stub.explanation, ctx.questionResponse.explanation)
        assertEquals(stub.level, ctx.questionResponse.level)
    }

    @Test
    fun badId() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.DELETE,
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
            command = QuizCommand.DELETE,
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
            command = QuizCommand.DELETE,
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
