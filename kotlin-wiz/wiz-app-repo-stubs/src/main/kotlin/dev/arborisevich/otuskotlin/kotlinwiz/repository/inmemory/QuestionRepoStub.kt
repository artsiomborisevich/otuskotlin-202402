package dev.arborisevich.otuskotlin.kotlinwiz.repository.inmemory

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionFilterRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionIdRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IDbQuestionResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IDbQuestionsResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub

class QuestionRepoStub() : IRepoQuestion {

    override suspend fun createQuestion(rq: DbQuestionRequest): IDbQuestionResponse {
        return DbQuestionResponseOk(
            data = QuizQuestionStub.get(),
        )
    }

    override suspend fun readQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse {
        return DbQuestionResponseOk(
            data = QuizQuestionStub.get(),
        )
    }

    override suspend fun updateQuestion(rq: DbQuestionRequest): IDbQuestionResponse {
        return DbQuestionResponseOk(
            data = QuizQuestionStub.get(),
        )
    }

    override suspend fun deleteQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse {
        return DbQuestionResponseOk(
            data = QuizQuestionStub.get(),
        )
    }

    override suspend fun searchQuestion(rq: DbQuestionFilterRequest): IDbQuestionsResponse {
        return DbQuestionsResponseOk(
            data = QuizQuestionStub.prepareSearchExpertList(filter = "", QuizQuestionLevel.EXPERT),
        )
    }
}
