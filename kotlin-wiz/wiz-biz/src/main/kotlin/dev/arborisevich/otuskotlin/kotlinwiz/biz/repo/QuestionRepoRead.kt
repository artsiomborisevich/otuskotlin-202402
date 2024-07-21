package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionIdRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErr
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseErrWithData
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Reading a question from the database"
    on { state == QuizState.RUNNING }
    handle {
        val request = DbQuestionIdRequest(questionValidated)
        when (val result = questionRepo.readQuestion(request)) {
            is DbQuestionResponseOk -> questionRepoRead = result.data
            is DbQuestionResponseErr -> fail(result.errors)
            is DbQuestionResponseErrWithData -> {
                fail(result.errors)
                questionRepoRead = result.data
            }
        }
    }
}
