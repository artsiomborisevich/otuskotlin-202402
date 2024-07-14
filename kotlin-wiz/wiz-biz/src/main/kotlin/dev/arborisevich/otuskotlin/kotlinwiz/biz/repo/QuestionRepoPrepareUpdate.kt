package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Preparing data for saving in the database: combining data read from the database, " +
            "and data received from the user"
    on { state == QuizState.RUNNING }
    handle {
        questionRepoPrepare = questionRepoRead.deepCopy().apply {
            text = questionValidated.text
            answer = questionValidated.answer
            explanation = questionValidated.explanation
            answerOptions = questionValidated.answerOptions
            level = questionValidated.level
            lock = questionValidated.lock
        }
    }
}
