package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErr
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErrWithData
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Adding a question to the database"
    on { state == QuizState.RUNNING }
    handle {
        val request = DbQuestionRequest(questionRepoPrepare)
        when(val result = questionRepo.createQuestion(request)) {
            is DbQuestionResponseOk -> questionRepoDone = result.data
            is DbQuestionResponseErr -> fail(result.errors)
            is DbQuestionResponseErrWithData -> fail(result.errors)
        }
    }
}
