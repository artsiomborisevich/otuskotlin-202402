package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizUserId
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Preparing an object for saving in the database"
    on { state == QuizState.RUNNING }
    handle {
        questionRepoPrepare = questionValidated.deepCopy()
        // TODO
        questionRepoPrepare.userId = QuizUserId.NONE
    }
}
