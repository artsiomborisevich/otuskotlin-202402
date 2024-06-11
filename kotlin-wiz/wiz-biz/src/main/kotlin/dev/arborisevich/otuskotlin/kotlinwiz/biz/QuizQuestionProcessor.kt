package dev.arborisevich.otuskotlin.kotlinwiz.biz

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub

@Suppress("unused", "RedundantSuspendModifier")
class QuizQuestionProcessor(val corSettings: QuizCoreSettings) {
    suspend fun exec(ctx: QuizContext) {
        ctx.questionResponse = QuizQuestionStub.get()
        ctx.questionsResponse = QuizQuestionStub.prepareSearchBeginnerList(
            "What",
            QuizQuestionLevel.BEGINNER)
            .toMutableList()

        ctx.state = QuizState.RUNNING
    }
}
