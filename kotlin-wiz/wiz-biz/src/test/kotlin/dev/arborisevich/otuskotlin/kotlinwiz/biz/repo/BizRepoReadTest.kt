package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.QuestionRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val command = QuizCommand.READ
    private val initQuestion = QuizQuestion(
        id = QuizQuestionId("123"),
        text = "abc",
        answer = "abc",
        answerOptions = listOf(
            QuizAnswerOption("1", "answerOption1"),
            QuizAnswerOption("2", "answerOption2"),
            QuizAnswerOption("3", "answerOption3"),
            QuizAnswerOption("4", "answerOption4"),
        ),
        explanation = "explanation",
        level = QuizQuestionLevel.ADVANCED,
    )
    private val repo = QuestionRepositoryMock(
        invokeReadQuestion = {
            DbQuestionResponseOk(
                data = initQuestion,
            )
        }
    )
    private val settings = QuizCoreSettings(repoTest = repo)
    private val processor = QuizQuestionProcessor(settings)

    @Test
    fun repoReQuestionSuccessTest() = runTest {
        val ctx = QuizContext(
            command = command,
            state = QuizState.NONE,
            workMode = QuizWorkMode.TEST,
            questionRequest = QuizQuestion(
                id = QuizQuestionId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(QuizState.FINISHING, ctx.state)
        assertEquals(initQuestion.id, ctx.questionResponse.id)
        assertEquals(initQuestion.text, ctx.questionResponse.text)
        assertEquals(initQuestion.answer, ctx.questionResponse.answer)
        assertEquals(initQuestion.explanation, ctx.questionResponse.explanation)
        assertEquals(initQuestion.answerOptions, ctx.questionResponse.answerOptions)
        assertEquals(initQuestion.level, ctx.questionResponse.level)
    }

    @Test
    fun repoReQuestionNotFoundTest() = repoNotFoundTest(command)
}
