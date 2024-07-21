package dev.arborisevich.otuskotlin.kotlinwiz.repo.tests

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.*
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class QuestionRepositoryMockTest{
    private val repo = QuestionRepositoryMock(
        invokeCreateQuestion = { DbQuestionResponseOk(QuizQuestionStub.prepareResult { text = "create" }) },
        invokeReadQuestion = { DbQuestionResponseOk(QuizQuestionStub.prepareResult { text = "read" }) },
        invokeUpdateQuestion = { DbQuestionResponseOk(QuizQuestionStub.prepareResult { text = "update" }) },
        invokeDeleteQuestion = { DbQuestionResponseOk(QuizQuestionStub.prepareResult { text = "delete" }) },
        invokeSearchQuestion = { DbQuestionsResponseOk(listOf(QuizQuestionStub.prepareResult { text = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createQuestion(DbQuestionRequest(QuizQuestion()))
        assertIs<DbQuestionResponseOk>(result)
        assertEquals("create", result.data.text)
    }

    @Test
    fun mockReQuestion() = runTest {
        val result = repo.readQuestion(DbQuestionIdRequest(QuizQuestion()))
        assertIs<DbQuestionResponseOk>(result)
        assertEquals("read", result.data.text)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateQuestion(DbQuestionRequest(QuizQuestion()))
        assertIs<DbQuestionResponseOk>(result)
        assertEquals("update", result.data.text)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteQuestion(DbQuestionIdRequest(QuizQuestion()))
        assertIs<DbQuestionResponseOk>(result)
        assertEquals("delete", result.data.text)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchQuestion(DbQuestionFilterRequest())
        assertIs<DbQuestionsResponseOk>(result)
        assertEquals("search", result.data.first().text)
    }
}