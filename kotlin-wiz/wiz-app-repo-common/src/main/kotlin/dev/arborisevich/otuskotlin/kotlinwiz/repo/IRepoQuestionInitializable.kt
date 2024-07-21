package dev.arborisevich.otuskotlin.kotlinwiz.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion

interface IRepoQuestionInitializable: IRepoQuestion {

    fun save(questions: Collection<QuizQuestion>): Collection<QuizQuestion>
}
