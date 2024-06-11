package dev.arborisevich.otuskotlin.kotlinwiz.stubs

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStubBeginnerQuest.QUIZ_QUESTION_BEGINNER1
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStubBeginnerQuest.QUIZ_QUESTION_EXPERT1

object QuizQuestionStub {

    fun get(): QuizQuestion = QUIZ_QUESTION_BEGINNER1.copy()

    fun prepareResult(block: QuizQuestion.() -> Unit): QuizQuestion = get().apply(block)

    fun prepareSearchBeginnerList(filter: String, level: QuizQuestionLevel) = listOf(
        quizQuestionBegginer("d-666-01", filter, level),
        quizQuestionBegginer("d-666-02", filter, level),
        quizQuestionBegginer("d-666-03", filter, level),
        quizQuestionBegginer("d-666-04", filter, level),
        quizQuestionBegginer("d-666-05", filter, level),
        quizQuestionBegginer("d-666-06", filter, level),
    )

    fun prepareSearchExpertList(filter: String, level: QuizQuestionLevel) = listOf(
        quizQuestionExpert("s-666-01", filter, level),
        quizQuestionExpert("s-666-02", filter, level),
        quizQuestionExpert("s-666-03", filter, level),
        quizQuestionExpert("s-666-04", filter, level),
        quizQuestionExpert("s-666-05", filter, level),
        quizQuestionExpert("s-666-06", filter, level),
    )

    private fun quizQuestionBegginer(id: String, filter: String, level: QuizQuestionLevel) =
        quizQuestion(QUIZ_QUESTION_BEGINNER1, id = id, filter = filter, level = level)

    private fun quizQuestionExpert(id: String, filter: String, level: QuizQuestionLevel) =
        quizQuestion(QUIZ_QUESTION_EXPERT1, id = id, filter = filter, level = level)

    private fun quizQuestion(base: QuizQuestion, id: String, filter: String, level: QuizQuestionLevel) = base.copy(
        id = QuizQuestionId(id),
        text = "$filter $id",
        answer = "answer $filter $id",
        level = level,
    )

}
