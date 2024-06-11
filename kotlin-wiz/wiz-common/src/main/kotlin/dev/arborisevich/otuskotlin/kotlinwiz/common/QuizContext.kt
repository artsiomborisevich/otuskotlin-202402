package dev.arborisevich.otuskotlin.kotlinwiz.common

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionFilter
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizRequestId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizWorkMode
import dev.arborisevich.otuskotlin.kotlinwiz.common.stubs.QuizStubs
import kotlinx.datetime.Instant

data class QuizContext(
    var command: QuizCommand = QuizCommand.NONE,
    var state: QuizState = QuizState.NONE,
    val errors: MutableList<QuizError> = mutableListOf(),

    var workMode: QuizWorkMode = QuizWorkMode.PROD,
    var stubCase: QuizStubs = QuizStubs.NONE,

    var requestId: QuizRequestId = QuizRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var questionRequest: QuizQuestion = QuizQuestion(),
    var questionFilterRequest: QuizQuestionFilter = QuizQuestionFilter(),

    var questionResponse: QuizQuestion = QuizQuestion(),
    var questionsResponse: MutableList<QuizQuestion> = mutableListOf(),

    )