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
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErr
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.QuestionRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val command = QuizCommand.DELETE
    private val initQuestion = QuizQuestion(
        id = QuizQuestionId("123"),
        text = "xyz",
        answer = "xyz",
        answerOptions = listOf(
            QuizAnswerOption("1", "answerOption1"),
            QuizAnswerOption("2", "answerOption2"),
            QuizAnswerOption("3", "answerOption3"),
            QuizAnswerOption("4", "answerOption4"),
        ),
        explanation = "explanation",
        level = QuizQuestionLevel.ADVANCED,
        lock = QuizQuestionLock("123-234-abc-ABC"),
    )

    private val repo = QuestionRepositoryMock(
        invokeReadQuestion = {
            DbQuestionResponseOk(
                data = initQuestion,
            )
        },
        invokeDeleteQuestion = {
            if (it.id == initQuestion.id)
                DbQuestionResponseOk(
                    data = initQuestion
                )
            else DbQuestionResponseErr()
        }
    )
    private val settings by lazy {
        QuizCoreSettings(
            repoTest = repo
        )
    }
    private val processor = QuizQuestionProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val questionToUpdate = QuizQuestion(
            id = QuizQuestionId("123"),
            lock = QuizQuestionLock("123-234-abc-ABC"),
        )
        val ctx = QuizContext(
            command = command,
            state = QuizState.NONE,
            workMode = QuizWorkMode.TEST,
            questionRequest = questionToUpdate,
        )
        processor.exec(ctx)

        assertEquals(QuizState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initQuestion.id, ctx.questionResponse.id)
        assertEquals(initQuestion.text, ctx.questionResponse.text)
        assertEquals(initQuestion.answer, ctx.questionResponse.answer)
        assertEquals(initQuestion.explanation, ctx.questionResponse.explanation)
        assertEquals(initQuestion.answerOptions, ctx.questionResponse.answerOptions)
        assertEquals(initQuestion.level, ctx.questionResponse.level)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
