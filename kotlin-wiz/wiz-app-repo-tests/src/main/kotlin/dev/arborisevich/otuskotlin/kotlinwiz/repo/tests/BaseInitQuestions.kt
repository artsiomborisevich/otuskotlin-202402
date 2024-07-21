package dev.arborisevich.otuskotlin.kotlinwiz.repo.tests

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock

abstract class BaseInitQuestions(private val op: String): IInitObjects<QuizQuestion> {
    open val lockOld: QuizQuestionLock = QuizQuestionLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: QuizQuestionLock = QuizQuestionLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        lock: QuizQuestionLock = lockOld,
    ) = QuizQuestion(
        id = QuizQuestionId("question-repo-$op-$suf"),
        text = "$suf stub",
        answer = "$suf stub answer",
        answerOptions = listOf(QuizAnswerOption("1","first option-$suf")),
        explanation = "$suf stub explanation",
        level = QuizQuestionLevel.EXPERT,
        lock = lock,
    )
}
