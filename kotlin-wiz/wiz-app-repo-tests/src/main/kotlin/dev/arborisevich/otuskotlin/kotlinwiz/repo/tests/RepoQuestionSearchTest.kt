package dev.arborisevich.otuskotlin.kotlinwiz.repo.tests

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionFilterRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoQuestionSearchTest {
    abstract val repo: IRepoQuestion

    protected open val initializedObjects: List<QuizQuestion> = initObjects

    @Test
    fun searchText() = runRepoTest {
        val result = repo.searchQuestion(DbQuestionFilterRequest("search"))

        assertIs<DbQuestionsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitQuestions("search") {

        override val initObjects: List<QuizQuestion> = listOf(
            createInitTestModel("text1"),
            createInitTestModel("searchtext2"),
            createInitTestModel("text3"),
            createInitTestModel("searchtext4"),
            createInitTestModel("text5"),
        )
    }
}
