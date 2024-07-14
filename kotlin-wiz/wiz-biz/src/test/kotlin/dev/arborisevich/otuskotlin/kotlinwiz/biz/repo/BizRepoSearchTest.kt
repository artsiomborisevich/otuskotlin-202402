package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionFilter
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.QuestionRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val command = QuizCommand.SEARCH
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
        invokeSearchQuestion = {
            DbQuestionsResponseOk(
                data = listOf(initQuestion),
            )
        }
    )
    private val settings = QuizCoreSettings(repoTest = repo)
    private val processor = QuizQuestionProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = QuizContext(
            command = command,
            state = QuizState.NONE,
            workMode = QuizWorkMode.TEST,
            questionFilterRequest = QuizQuestionFilter(
                searchString = "abc",
            ),
        )
        processor.exec(ctx)
        assertEquals(QuizState.FINISHING, ctx.state)
        assertEquals(1, ctx.questionsResponse.size)
    }
}
