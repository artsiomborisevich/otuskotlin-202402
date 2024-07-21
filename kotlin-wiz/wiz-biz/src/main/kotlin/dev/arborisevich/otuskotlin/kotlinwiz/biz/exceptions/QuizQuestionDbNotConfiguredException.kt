package dev.arborisevich.otuskotlin.kotlinwiz.biz.exceptions

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode

class QuizQuestionDbNotConfiguredException(val workMode: QuizWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
