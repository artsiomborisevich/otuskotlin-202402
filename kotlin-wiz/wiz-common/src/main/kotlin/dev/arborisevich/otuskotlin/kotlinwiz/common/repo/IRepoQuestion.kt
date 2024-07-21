package dev.arborisevich.otuskotlin.kotlinwiz.common.repo

interface IRepoQuestion {
    
    suspend fun createQuestion(rq: DbQuestionRequest): IDbQuestionResponse
    suspend fun readQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse
    suspend fun updateQuestion(rq: DbQuestionRequest): IDbQuestionResponse
    suspend fun deleteQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse
    suspend fun searchQuestion(rq: DbQuestionFilterRequest): IDbQuestionsResponse
    
    companion object {
        val NONE = object : IRepoQuestion {
            override suspend fun createQuestion(rq: DbQuestionRequest): IDbQuestionResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateQuestion(rq: DbQuestionRequest): IDbQuestionResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchQuestion(rq: DbQuestionFilterRequest): IDbQuestionsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
