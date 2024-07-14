package dev.arborisevich.otuskotlin.kotlinwiz.common

import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.QuizLoggerProvider

data class QuizCoreSettings(
    val loggerProvider: QuizLoggerProvider = QuizLoggerProvider(),
    val repoStub: IRepoQuestion = IRepoQuestion.NONE,
    val repoTest: IRepoQuestion = IRepoQuestion.NONE,
    val repoProd: IRepoQuestion = IRepoQuestion.NONE,
) {
    companion object {
        val NONE = QuizCoreSettings()
    }
}
