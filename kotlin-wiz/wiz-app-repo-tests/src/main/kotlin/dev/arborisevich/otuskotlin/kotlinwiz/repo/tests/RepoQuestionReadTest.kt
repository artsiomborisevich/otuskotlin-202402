package dev.arborisevich.otuskotlin.kotlinwiz.repo.tests

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionIdRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErr
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoQuestionReadTest {
    abstract val repo: IRepoQuestion
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readQuestion(DbQuestionIdRequest(readSucc.id))

        println("XxX"+result.toString())
        assertIs<DbQuestionResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readQuestion(DbQuestionIdRequest(notFoundId))

        assertIs<DbQuestionResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitQuestions("read") {
        override val initObjects: List<QuizQuestion> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = QuizQuestionId("ad-repo-read-notFound")

    }
}
