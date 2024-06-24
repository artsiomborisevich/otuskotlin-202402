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

fun validationTextCorrect(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = "text",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1","answerOption1"),
                QuizAnswerOption("2","answerOption2")
            ),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(QuizState.FAILING, ctx.state)
    assertEquals("text", ctx.questionValidated.text)
}

fun validationTextTrim(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = " \n\t abc \t\n ",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1","answerOption1"),
                QuizAnswerOption("2","answerOption2")
            ),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("123-234-abc-ABC"),
        ),
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(QuizState.FAILING, ctx.state)
    assertEquals("abc", ctx.questionValidated.text)
}

fun validationTextEmpty(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = "",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1","answerOption1"),
                QuizAnswerOption("2","answerOption2")
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
    assertEquals("text", error?.field)
    assertContains(error?.message ?: "", "text")
}

fun validationTextSymbols(command: QuizCommand, processor: QuizQuestionProcessor) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = stub.id,
            text = "!@#$%^&*(),.{}",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1","answerOption1"),
                QuizAnswerOption("2","answerOption2")
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
    assertEquals("text", error?.field)
    assertContains(error?.message ?: "", "text")
}
