package dev.arborisevich.otuskotlin.kotlinwiz.biz.validation

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = QuizCommand.UPDATE

    @Test
    fun correctText() = validationTextCorrect(command, processor)
    @Test
    fun trimText() = validationTextTrim(command, processor)
    @Test
    fun emptyText() = validationTextEmpty(command, processor)
    @Test
    fun badSymbolsText() = validationTextSymbols(command, processor)


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

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)

    @Test
    fun correctLock() = validationLockCorrect(command, processor)
    @Test
    fun trimLock() = validationLockTrim(command, processor)
    @Test
    fun emptyLock() = validationLockEmpty(command, processor)
    @Test
    fun badFormatLock() = validationLockFormat(command, processor)

}
