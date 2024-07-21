package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.repo.QuestionRepoInMemory
import dev.arborisevich.otuskotlin.kotlinwiz.repo.QuestionRepoInitialized
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub

abstract class BaseBizValidationTest {
    protected abstract val command: QuizCommand
    private val repo = QuestionRepoInitialized(
        repo = QuestionRepoInMemory(),
        initObjects = listOf(
            QuizQuestionStub.get(),
        ),
    )
    private val settings by lazy { QuizCoreSettings(repoTest = repo) }
    protected val processor by lazy { QuizQuestionProcessor(settings) }
}
