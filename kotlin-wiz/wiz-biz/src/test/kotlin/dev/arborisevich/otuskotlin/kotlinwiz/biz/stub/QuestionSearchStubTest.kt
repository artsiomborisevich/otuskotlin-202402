package dev.arborisevich.otuskotlin.kotlinwiz.biz.stub

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionFilter
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class QuestionSearchStubTest {

    private val processor = QuizQuestionProcessor()
    val filter = QuizQuestionFilter(searchString = "Kotlin")

    @Test
    fun read() = runTest {

        val ctx = QuizContext(
            command = QuizCommand.SEARCH,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.SUCCESS,
            questionFilterRequest = filter,
        )

        processor.exec(ctx)

        assertTrue(ctx.questionsResponse.size > 1)

        val first = ctx.questionsResponse.firstOrNull() ?: fail("Empty response list")

        assertTrue(first.text.contains(filter.searchString))
        assertTrue(first.answer.contains(filter.searchString))

        with(QuizQuestionStub.get()) {
            assertEquals(level, first.level)
            assertEquals(explanation, first.explanation)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.SEARCH,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.BAD_ID,
            questionFilterRequest = filter,
        )

        processor.exec(ctx)

        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.SEARCH,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.DB_ERROR,
            questionFilterRequest = filter,
        )

        processor.exec(ctx)

        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = QuizContext(
            command = QuizCommand.SEARCH,
            state = QuizState.NONE,
            workMode = QuizWorkMode.STUB,
            stubCase = QuizStubs.BAD_TEXT,
            questionFilterRequest = filter,
        )

        processor.exec(ctx)

        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
