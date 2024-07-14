package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorRepoConcurrency
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
  Optimistic locking check. If it is not equal to that stored in the database, then the request data is out of date
 and you need to update them manually
    """.trimIndent()
    on { state == QuizState.RUNNING && questionValidated.lock != questionRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(questionRepoRead, questionValidated.lock).errors)
    }
}
