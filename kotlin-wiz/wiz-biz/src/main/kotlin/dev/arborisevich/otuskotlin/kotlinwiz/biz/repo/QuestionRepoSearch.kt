package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionFilterRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseErr
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Search for questions in the database by filter"
    on { state == QuizState.RUNNING }
    handle {
        val request = DbQuestionFilterRequest(
            textFilter = questionFilterValidated.searchString,
        )
        when(val result = questionRepo.searchQuestion(request)) {
            is DbQuestionsResponseOk -> questionsRepoDone = result.data.toMutableList()
            is DbQuestionsResponseErr -> fail(result.errors)
        }
    }
}
