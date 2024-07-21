package dev.arborisevich.otuskotlin.kotlinwiz.stubs

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizAnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionPermissionsClient

object QuizQuestionStubBeginnerQuest {
    val answerOption1 = QuizAnswerOption(id = "1", answerText = "var is mutable, val is immutable")
    val answerOption2 = QuizAnswerOption(id = "2", answerText = "val is mutable, var is immutable")
    val answerOption3 = QuizAnswerOption(id = "3", answerText = "Both var and val are mutable")
    val answerOption4 = QuizAnswerOption(id = "4", answerText = "Both var and val are immutable")

    val answerOptions = listOf(answerOption1, answerOption2, answerOption3, answerOption4)

    val QUIZ_QUESTION_BEGINNER1: QuizQuestion
        get() = QuizQuestion(
            id = QuizQuestionId("666"),
            text = "What is the difference between var and val in Kotlin?",
            answer = "var is mutable, val is immutable",
            answerOptions = answerOptions,
            explanation = "In Kotlin, 'var' is used to declare a mutable variable, while 'val' is used to declare an immutable variable.",
            level = QuizQuestionLevel.BEGINNER,
            lock = QuizQuestionLock("lock"),
            permissionsClient = mutableSetOf(
                QuizQuestionPermissionsClient.READ,
                QuizQuestionPermissionsClient.UPDATE,
                QuizQuestionPermissionsClient.DELETE
            )
        )
    val  QUIZ_QUESTION_EXPERT1 = QUIZ_QUESTION_BEGINNER1.copy(level = QuizQuestionLevel.EXPERT)
}
