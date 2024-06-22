package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import kotlin.test.Test

// look at an example of a validation test assembled from test wrapper functions
class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: QuizCommand = QuizCommand.CREATE

    @Test
    fun correctText() = validationTextCorrect(command, processor)
    @Test
    fun trimText() = validationTextTrim(command, processor)
    @Test
    fun emptyText() = validationTextEmpty(command, processor)
    @Test
    fun badSymbolsTitle() = validationTextSymbols(command, processor)

    @Test
    fun correctAnswer() = validationAnswerCorrect(command, processor)
    @Test
    fun trimAnswer() = validationAnswerTrim(command, processor)
    @Test
    fun emptyAnswer() = validationAnswerEmpty(command, processor)
    @Test
    fun badSymbolsAnswer() = validationAnswerSymbols(command, processor)

    @Test
    fun correctExplanation() = validationExplanationCorrect(command, processor)
    @Test
    fun trimExplanation() = validationExplanationTrim(command, processor)
    @Test
    fun emptyExplanation() = validationExplanationEmpty(command, processor)
    @Test
    fun badSymbolsExplanation() = validationExplanationSymbols(command, processor)


    @Test
    fun correctAnswerOptions() = validationAnswerOptionsCorrect(command, processor)
    @Test
    fun NoAnswerOptions() = validationNoAnswerOptions(command, processor)
    @Test
    fun trimAnswerOption() = validationAnswerOptionTrim(command, processor)
    @Test
    fun emptyAnswerOption() = validationAnswerOptionEmpty(command, processor)
    @Test
    fun badSymbolsAnswerOption() = validationAnswerOptionSymbols(command, processor)
}
