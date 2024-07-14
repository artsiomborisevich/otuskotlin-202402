package dev.arborisevich.otuskotlin.kotlinwiz.repo.tests

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoQuestionDeleteTest {
    abstract val repo: IRepoQuestion
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = QuizQuestionId("ad-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteQuestion(DbQuestionIdRequest(deleteSucc.id, lock = lockOld))

        assertIs<DbQuestionResponseOk>(result)
        assertEquals(deleteSucc.text, result.data.text)
        assertEquals(deleteSucc.answer, result.data.answer)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readQuestion(DbQuestionIdRequest(notFoundId, lock = lockOld))

        assertIs<DbQuestionResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteQuestion(DbQuestionIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbQuestionResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitQuestions("delete") {
        override val initObjects: List<QuizQuestion> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
