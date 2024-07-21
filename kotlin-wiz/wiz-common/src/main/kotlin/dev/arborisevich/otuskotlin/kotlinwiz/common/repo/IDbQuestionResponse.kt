package dev.arborisevich.otuskotlin.kotlinwiz.common.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion

sealed interface IDbQuestionResponse: IDbResponse<QuizQuestion>

data class DbQuestionResponseOk(
    val data: QuizQuestion
): IDbQuestionResponse

data class DbQuestionResponseErr(
    val errors: List<QuizError> = emptyList()
): IDbQuestionResponse {

    constructor(err: QuizError): this(listOf(err))
}

data class DbQuestionResponseErrWithData(
    val data: QuizQuestion,
    val errors: List<QuizError> = emptyList()
): IDbQuestionResponse {

    constructor(question: QuizQuestion, err: QuizError): this(question, listOf(err))
}
