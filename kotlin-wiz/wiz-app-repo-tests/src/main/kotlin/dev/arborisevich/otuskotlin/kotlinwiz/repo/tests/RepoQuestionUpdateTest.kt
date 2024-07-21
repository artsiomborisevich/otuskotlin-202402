package dev.arborisevich.otuskotlin.kotlinwiz.repo.tests

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErr
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErrWithData
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoQuestionUpdateTest {
    abstract val repo: IRepoQuestion
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = QuizQuestionId("ad-repo-update-not-found")
    protected val lockBad = QuizQuestionLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = QuizQuestionLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        QuizQuestion(
            id = updateSucc.id,
            text = "update object",
            answer = "update object answer",
            answerOptions = listOf(
                QuizAnswerOption("100", "update option"),
            ),
            explanation = "update object explanation",
            level = QuizQuestionLevel.EXPERT,
            lock = initObjects.first().lock,
            )
    }

    private val reqUpdateNotFound = QuizQuestion(
        id = updateIdNotFound,
        text = "update object not found",
        answer = "update object answer not found",
        answerOptions = listOf(
            QuizAnswerOption("100", "update option not found"),
        ),
        explanation = "update object explanation not found",
        level = QuizQuestionLevel.EXPERT,
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        QuizQuestion(
            id = updateConc.id,
            text = "update object not found",
            answer = "update object answer not found",
            answerOptions = listOf(
                QuizAnswerOption("100", "update option not found"),
            ),
            explanation = "update object explanation not found",
            level = QuizQuestionLevel.EXPERT,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateQuestion(DbQuestionRequest(reqUpdateSucc))

        println("XxX"+result.toString())
        assertIs<DbQuestionResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.text, result.data.text)
        assertEquals(reqUpdateSucc.answer, result.data.answer)
        assertEquals(reqUpdateSucc.explanation, result.data.explanation)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateQuestion(DbQuestionRequest(reqUpdateNotFound))
        assertIs<DbQuestionResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateQuestion(DbQuestionRequest(reqUpdateConc))
        assertIs<DbQuestionResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitQuestions("update") {
        override val initObjects: List<QuizQuestion> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
