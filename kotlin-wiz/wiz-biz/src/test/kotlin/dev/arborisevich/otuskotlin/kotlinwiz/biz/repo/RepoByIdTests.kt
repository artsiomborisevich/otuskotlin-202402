package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorNotFound
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.QuestionRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initQuestion = QuizQuestion(
    id = QuizQuestionId("123"),
    text = "abc",
    answer = "abc",
    answerOptions = listOf(
        QuizAnswerOption("1","answerOption1"),
        QuizAnswerOption("2","answerOption2"),
        QuizAnswerOption("3","answerOption3"),
        QuizAnswerOption("4","answerOption4"),
    ),
    explanation = "explanation",
    level = QuizQuestionLevel.ADVANCED,
)
private val repo = QuestionRepositoryMock(
        invokeReadQuestion = {
            if (it.id == initQuestion.id) {
                DbQuestionResponseOk(
                    data = initQuestion,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = QuizCoreSettings(repoTest = repo)
private val processor = QuizQuestionProcessor(settings)

fun repoNotFoundTest(command: QuizCommand) = runTest {
    val ctx = QuizContext(
        command = command,
        state = QuizState.NONE,
        workMode = QuizWorkMode.TEST,
        questionRequest = QuizQuestion(
            id = QuizQuestionId("12345"),
            text = "text",
            answer = "answer",
            answerOptions = listOf(
                QuizAnswerOption("1","answerOption1"),
                QuizAnswerOption("2","answerOption2"),
                QuizAnswerOption("3","answerOption3"),
                QuizAnswerOption("4","answerOption4"),
            ),
            explanation = "explanation",
            level = QuizQuestionLevel.ADVANCED,
            lock = QuizQuestionLock("123"),
        ),
    )
    processor.exec(ctx)
    assertEquals(QuizState.FAILING, ctx.state)
    assertEquals(QuizQuestion(), ctx.questionResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
