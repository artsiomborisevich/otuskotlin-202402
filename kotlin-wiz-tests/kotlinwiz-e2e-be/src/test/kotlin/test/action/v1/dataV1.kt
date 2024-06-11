package dev.arborisevich.otuskotlin.kotlinwiz.test.action.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDebug
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugMode
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugStubs

val debug = QuestionDebug(mode = QuestionRequestDebugMode.STUB, stub = QuestionRequestDebugStubs.SUCCESS)

val answerOption1 = AnswerOption(id = "1", text = "var is mutable, val is immutable")
val answerOption2 = AnswerOption(id = "2", text = "val is mutable, var is immutable")
val answerOption3 = AnswerOption(id = "3", text = "Both var and val are mutable")
val answerOption4 = AnswerOption(id = "4", text = "Both var and val are immutable")

val answerOptions = listOf(answerOption1, answerOption2, answerOption3, answerOption4)

val someCreateQuestion = QuestionCreateObject(
    text = "What is the difference between var and val in Kotlin?",
    answerOptions = answerOptions,
    answer = "var is mutable, val is immutable",
    explanation = "In Kotlin, 'var' is used to declare a mutable variable, while 'val' is used to declare an immutable variable.",
    level = QuestionLevel.BEGINNER
)
