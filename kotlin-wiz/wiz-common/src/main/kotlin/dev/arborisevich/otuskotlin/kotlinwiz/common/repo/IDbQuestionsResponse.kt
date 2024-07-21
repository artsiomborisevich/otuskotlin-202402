package dev.arborisevich.otuskotlin.kotlinwiz.common.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion

sealed interface IDbQuestionsResponse: IDbResponse<List<QuizQuestion>>

data class DbQuestionsResponseOk(
    val data: List<QuizQuestion>
): IDbQuestionsResponse

@Suppress("unused")
data class DbQuestionsResponseErr(
    val errors: List<QuizError> = emptyList()
): IDbQuestionsResponse {
    
    constructor(err: QuizError): this(listOf(err))
}
