package dev.arborisevich.otuskotlin.kotlinwiz.app.config

import dev.arborisevich.otuskotlin.kotlinwiz.app.common.IQuizAppSettings
import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings

data class QuizAppSettings(
    override val corSettings: QuizCoreSettings,
    override val processor: QuizQuestionProcessor,
): IQuizAppSettings
