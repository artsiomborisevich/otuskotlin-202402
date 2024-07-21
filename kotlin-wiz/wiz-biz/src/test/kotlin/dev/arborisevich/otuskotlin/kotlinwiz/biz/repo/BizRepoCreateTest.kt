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
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizUserId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.QuestionRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = QuizUserId("321")
    private val command = QuizCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = QuestionRepositoryMock(
        invokeCreateQuestion = {
            DbQuestionResponseOk(
                data = QuizQuestion(
                    id = QuizQuestionId(uuid),
                    text = it.question.text,
                    answer = it.question.answer,
                    explanation = it.question.explanation,
                    answerOptions = it.question.answerOptions,
                    userId = userId,
                    level = it.question.level,
                )
            )
        }
    )
    private val settings = QuizCoreSettings(
        repoTest = repo
    )
    private val processor = QuizQuestionProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val answerOptions = listOf(
            QuizAnswerOption("1", "answerOption1"),
            QuizAnswerOption("2", "answerOption2"),
            QuizAnswerOption("3", "answerOption3"),
            QuizAnswerOption("4", "answerOption4"),
        )
        val ctx = QuizContext(
            command = command,
            state = QuizState.NONE,
            workMode = QuizWorkMode.TEST,
            questionRequest = QuizQuestion(
                text = "xyz",
                answer = "xyz",
                answerOptions = answerOptions,
                explanation = "explanation",
                level = QuizQuestionLevel.ADVANCED,
            ),
        )
        processor.exec(ctx)

        assertEquals(QuizState.FINISHING, ctx.state)
        assertNotEquals(QuizQuestionId.NONE, ctx.questionResponse.id)
        assertEquals("xyz", ctx.questionResponse.text)
        assertEquals("xyz", ctx.questionResponse.answer)
        assertEquals("explanation", ctx.questionResponse.explanation)
        assertEquals(answerOptions, ctx.questionResponse.answerOptions)
        assertEquals(QuizQuestionLevel.ADVANCED, ctx.questionResponse.level)
    }
}
