package dev.arborisevich.otuskotlin.kotlinwiz.repo.tests

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionFilterRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionIdRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IDbQuestionResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IDbQuestionsResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion

class QuestionRepositoryMock(
    private val invokeCreateQuestion: (DbQuestionRequest) -> IDbQuestionResponse = { DEFAULT_Question_SUCCESS_EMPTY_MOCK },
    private val invokeReadQuestion: (DbQuestionIdRequest) -> IDbQuestionResponse = { DEFAULT_Question_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateQuestion: (DbQuestionRequest) -> IDbQuestionResponse = { DEFAULT_Question_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteQuestion: (DbQuestionIdRequest) -> IDbQuestionResponse = { DEFAULT_Question_SUCCESS_EMPTY_MOCK },
    private val invokeSearchQuestion: (DbQuestionFilterRequest) -> IDbQuestionsResponse = { DEFAULT_QuestionS_SUCCESS_EMPTY_MOCK },
): IRepoQuestion {

    override suspend fun createQuestion(rq: DbQuestionRequest): IDbQuestionResponse {
        return invokeCreateQuestion(rq)
    }

    override suspend fun readQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse {
        return invokeReadQuestion(rq)
    }

    override suspend fun updateQuestion(rq: DbQuestionRequest): IDbQuestionResponse {
        return invokeUpdateQuestion(rq)
    }

    override suspend fun deleteQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse {
        return invokeDeleteQuestion(rq)
    }

    override suspend fun searchQuestion(rq: DbQuestionFilterRequest): IDbQuestionsResponse {
        return invokeSearchQuestion(rq)
    }

    companion object {
        val DEFAULT_Question_SUCCESS_EMPTY_MOCK = DbQuestionResponseOk(QuizQuestion())
        val DEFAULT_QuestionS_SUCCESS_EMPTY_MOCK = DbQuestionsResponseOk(emptyList())
    }
}
