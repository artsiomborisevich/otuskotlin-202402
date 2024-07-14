package dev.arborisevich.otuskotlin.kotlinwiz.biz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.biz.exceptions.QuizQuestionDbNotConfiguredException
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorSystem
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.fail
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.core.ICorChainDsl
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

fun ICorChainDsl<QuizContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
       Calculate the main working repository depending on the requested operating mode
    """.trimIndent()
    handle {
        questionRepo = when (workMode) {
            QuizWorkMode.TEST -> corSettings.repoTest
            QuizWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }

        if (workMode != QuizWorkMode.STUB && questionRepo == IRepoQuestion.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = QuizQuestionDbNotConfiguredException(workMode)
            )
        )
    }
}
