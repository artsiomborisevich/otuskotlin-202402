package dev.arborisevich.otuskotlin.kotlinwiz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion

/**
 * A delegate for all repositories that allows you to initialize the database with preloaded data
 */
class QuestionRepoInitialized(
    val repo: IRepoQuestionInitializable,
    initObjects: Collection<QuizQuestion> = emptyList(),
) : IRepoQuestionInitializable by repo {

    @Suppress("unused")
    val initializedObjects: List<QuizQuestion> = save(initObjects).toList()
}
