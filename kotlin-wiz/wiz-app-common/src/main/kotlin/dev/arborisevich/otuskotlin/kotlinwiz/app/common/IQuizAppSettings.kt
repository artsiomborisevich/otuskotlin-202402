package dev.arborisevich.otuskotlin.kotlinwiz.app.common

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings

interface IQuizAppSettings {
    val processor: QuizQuestionProcessor
    val corSettings: QuizCoreSettings
}
