package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = QuizQuestionId("666"),
            text = "abc",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1", "answerOption1"),
                QuizAnswerOption("2", "answerOption2")
            ),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("lock"),
        ),
    )

    processor.exec(ctx)

    println(ctx.errors)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(QuizState.FAILING, ctx.state)
}

fun validationIdTrim(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = QuizQuestionId(" \n\t 666 \n\t "),
            text = "abc",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1", "answerOption1"),
                QuizAnswerOption("2", "answerOption2")
            ),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("lock"),
        ),
    )
    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(QuizState.FAILING, ctx.state)
}

fun validationIdEmpty(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = QuizQuestionId(""),
            text = "abc",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1", "answerOption1"),
                QuizAnswerOption("2", "answerOption2")
            ),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("lock"),
        ),
    )
    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(QuizState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = QuizQuestionId("!@#\$%^&*(),.{}"),
            text = "abc",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1", "answerOption1"),
                QuizAnswerOption("2", "answerOption2")
            ),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(QuizState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
