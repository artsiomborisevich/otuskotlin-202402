package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionResponseObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.ResponseResult
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.and

fun haveResult(result: ResponseResult) = Matcher<IResponse> {
    MatcherResult(
        it.result == result,
        { "actual result ${it.result} but we expected $result" },
        { "result should not be $result" }
    )
}

val haveNoErrors = Matcher<IResponse> {
    MatcherResult(
        it.errors.isNullOrEmpty(),
        { "actual errors ${it.errors} but we expected no errors" },
        { "errors should not be empty" }
    )
}

val haveSuccessResult = haveResult(ResponseResult.SUCCESS) and haveNoErrors

val IResponse.question: QuestionResponseObject?
    get() = when (this) {
        is QuestionCreateResponse -> question
        is QuestionReadResponse -> question
        is QuestionUpdateResponse -> question
        is QuestionDeleteResponse -> question
        is QuestionSearchResponse -> question
        else -> throw IllegalArgumentException("Invalid response type: ${this::class}")
    }
