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
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.QuestionRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {

    private val command = QuizCommand.UPDATE
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
        lock = QuizQuestionLock("123-234-abc-ABC"),
    )

    private val repo = QuestionRepositoryMock(
        invokeReadQuestion = {
            DbQuestionResponseOk(
                data = initQuestion,
            )
        },
        invokeUpdateQuestion = {
            DbQuestionResponseOk(
                data = QuizQuestion(
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
            )
        }
    )
    private val settings = QuizCoreSettings(repoTest = repo)
    private val processor = QuizQuestionProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val questionToUpdate = QuizQuestion(
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
        val ctx = QuizContext(
            command = command,
            state = QuizState.NONE,
            workMode = QuizWorkMode.TEST,
            questionRequest = questionToUpdate,
        )
        processor.exec(ctx)

        assertEquals(QuizState.FINISHING, ctx.state)
        assertEquals(questionToUpdate.id, ctx.questionResponse.id)
        assertEquals(questionToUpdate.text, ctx.questionResponse.text)
        assertEquals(questionToUpdate.answer, ctx.questionResponse.answer)
        assertEquals(questionToUpdate.explanation, ctx.questionResponse.explanation)
        assertEquals(questionToUpdate.answerOptions, ctx.questionResponse.answerOptions)
        assertEquals(questionToUpdate.level, ctx.questionResponse.level)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
