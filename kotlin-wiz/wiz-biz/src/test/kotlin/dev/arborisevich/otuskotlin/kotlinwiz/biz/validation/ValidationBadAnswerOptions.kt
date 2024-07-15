package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = QuizQuestionStub.get()

fun validationAnswerOptionsCorrect(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val answerOptions = listOf(
        QuizAnswerOption("1", "answerOption1"),
        QuizAnswerOption("2", "answerOption2")
    )

    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = "abc",
            answer = "answer",
            answerOptions = answerOptions,
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("lock"),
        ),
    )
    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(QuizState.FAILING, ctx.state)
    assertEquals(answerOptions, ctx.questionValidated.answerOptions)
}

fun validationNoAnswerOptions(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = "abc",
            answer = "answer",
            answerOptions = emptyList(),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("lock"),
        ),
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(QuizState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("answerOptions", error?.field)
    assertContains(error?.message ?: "", "answerOptions")
}

fun validationAnswerOptionTrim(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val answerOptions = listOf(
        QuizAnswerOption("1", "\n\tanswerOption1 \n\t"),
        QuizAnswerOption("2", "\n\tanswerOption2 \n\t"),
    )
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = "abc",
            answer = "answer",
            answerOptions = answerOptions,
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("lock"),
        ),
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(QuizState.FAILING, ctx.state)

    val answerOptionsExpected = listOf(
        QuizAnswerOption("1", "answerOption1"),
        QuizAnswerOption("2", "answerOption2"),
    )
    assertEquals(answerOptionsExpected, ctx.questionValidated.answerOptions)
}

fun validationAnswerOptionEmpty(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = "abc",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1", "answerOption1"),
                QuizAnswerOption("2", "")
            ),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("lock"),
        ),
    )

    processor.exec(ctx)

    println(ctx.errors)
    assertEquals(1, ctx.errors.size)
    assertEquals(QuizState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("answerOptions", error?.field)
    assertContains(error?.message ?: "", "answerOption")
}

fun validationAnswerOptionSymbols(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = "abc",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1", "answerOption1"),
                QuizAnswerOption("2", "!@#$%^&*(),.{}")
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
    assertEquals("answerOptions", error?.field)
    assertContains(error?.message ?: "", "answerOptions")
}
