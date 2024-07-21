package dev.arborisevich.otuskotlin.kotlinwiz.repo.tests

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.repo.IRepoQuestionInitializable
import org.junit.Assert.assertNotEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoQuestionCreateTest {
    abstract val repo: IRepoQuestionInitializable
    protected open val uuidNew = QuizQuestionId("10000000-0000-0000-0000-000000000001")

    private val createObj = QuizQuestion(
        text = "text",
        answer = "answer",
        answerOptions = listOf(
            QuizAnswerOption("1","first option"),
            QuizAnswerOption("2","second option")
        ),
        explanation = "explanation",
        level = QuizQuestionLevel.BEGINNER,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createQuestion(DbQuestionRequest(createObj))
        val expected = createObj

        assertIs<DbQuestionResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.text, result.data.text)
        assertEquals(expected.answer, result.data.answer)
        assertEquals(expected.explanation, result.data.explanation)
        assertEquals(expected.level, result.data.level)
        assertNotEquals(QuizQuestionId.NONE, result.data.id)
    }

    companion object : BaseInitQuestions("create") {
        override val initObjects: List<QuizQuestion> = emptyList()
    }
}