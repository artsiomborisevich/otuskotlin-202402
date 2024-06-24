package dev.arborisevich.otuskotlin.kotlinwiz.biz

import dev.arborisevich.otuskotlin.kotlinwiz.biz.general.initStatus
import dev.arborisevich.otuskotlin.kotlinwiz.biz.general.operation
import dev.arborisevich.otuskotlin.kotlinwiz.biz.general.stubs
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubCreateSuccess
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubDbError
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubDeleteSuccess
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubNoCase
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubReadSuccess
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubSearchSuccess
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubUpdateSuccess
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubValidationBadAnswer
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubValidationBadAnswerOptions
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubValidationBadExplanation
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubValidationBadId
import dev.arborisevich.otuskotlin.kotlinwiz.biz.stubs.stubValidationBadText
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.finishQuestionFilterValidation
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.finishQuestionValidation
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateAnswerHasContent
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateAnswerNotEmpty
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateAnswerOptionHasContent
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateAnswerOptionNotEmpty
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateAnswerOptionsNotEmpty
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateExplanationHasContent
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateExplanationNotEmpty
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateIdNotEmpty
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateIdProperFormat
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateLockNotEmpty
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateLockProperFormat
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateSearchStringLength
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateTextHasContent
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validateTextNotEmpty
import dev.arborisevich.otuskotlin.kotlinwiz.biz.validation.validation
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.core.rootChain
import dev.arborisevich.otuskotlin.kotlinwiz.core.worker

class QuizQuestionProcessor(
    private val corSettings: QuizCoreSettings = QuizCoreSettings.NONE
) {
    suspend fun exec(ctx: QuizContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<QuizContext> {
        initStatus("Initializing status")

        operation("Creating a question", QuizCommand.CREATE) {
            stubs("Processing stubs") {
                stubCreateSuccess("Simulating successful processing", corSettings)
                stubValidationBadText("Simulating a text validation error")
                stubValidationBadAnswer("Simulating an answer validation error")
                stubValidationBadAnswerOptions("Simulating an answer option validation error")
                stubValidationBadExplanation("Simulating an explanation validation error")
                stubDbError("Simulating a database error")
                stubNoCase("Error: The requested stub is invalid")
            }
            validation {
                worker("Copy fields in questionValidating") { questionValidating = questionRequest.deepCopy() }
                worker("Cleaning id") { questionValidating.id = QuizQuestionId.NONE }
                worker("Cleaning text") { questionValidating.text = questionValidating.text.trim() }
                worker("Cleaning answer") { questionValidating.answer = questionValidating.answer.trim() }
                worker("Cleaning explanation") {
                    questionValidating.explanation = questionValidating.explanation.trim()
                }
                worker("Cleaning answer options") {
                    questionValidating.answerOptions =
                        questionValidating.answerOptions.map { it.copy(answerText = it.answerText.trim()) }
                }
                validateTextNotEmpty("Checking that the text is not empty")
                validateTextHasContent("Character checking")
                validateAnswerNotEmpty("Checking that the answer is not empty")
                validateAnswerHasContent("Character checking")
                validateExplanationNotEmpty("Checking that the explanation is not empty")
                validateExplanationHasContent("Character checking")
                validateAnswerOptionsNotEmpty("Checking that the answerOptions is not empty")
                validateAnswerOptionNotEmpty("Checking that the answerOption is not empty")
                validateAnswerOptionHasContent("Character checking")

                finishQuestionValidation("Completing checks")
            }
        }

        operation("Get a question", QuizCommand.READ) {
            stubs("Processing stubs") {
                stubReadSuccess("Simulating successful processing", corSettings)
                stubValidationBadId("Simulating an id validation error")
                stubDbError("Simulating a database error")
                stubNoCase("Error: The requested stub is invalid")
            }
            validation {
                worker("Copy fields in questionValidating") { questionValidating = questionRequest.deepCopy() }
                worker("Cleaning id") {
                    questionValidating.id = QuizQuestionId(questionValidating.id.asString().trim())
                }
                validateIdNotEmpty("Checking for non-empty id")
                validateIdProperFormat("Checking id format")

                finishQuestionValidation("Completing checks")
            }
        }

        operation("Edit question", QuizCommand.UPDATE) {
            stubs("Processing stubs") {
                stubUpdateSuccess("Simulating successful processing", corSettings)
                stubValidationBadId("Simulating an id validation error")
                stubValidationBadText("Simulating a text validation error")
                stubValidationBadAnswer("Simulating an answer validation error")
                stubValidationBadAnswerOptions("Simulating an answer option validation error")
                stubValidationBadExplanation("Simulating an explanation validation error")
                stubDbError("Simulating a database error")
                stubNoCase("Error: The requested stub is invalid")
            }
            validation {
                worker("Copy fields in questionValidating") { questionValidating = questionRequest.deepCopy() }
                worker("Cleaning id") {
                    questionValidating.id = QuizQuestionId(questionValidating.id.asString().trim())
                }
                worker("Cleaning lock") {
                    questionValidating.lock = QuizQuestionLock(questionValidating.lock.asString().trim())
                }
                worker("Cleaning text") { questionValidating.text = questionValidating.text.trim() }
                worker("Cleaning answer") { questionValidating.answer = questionValidating.answer.trim() }
                worker("Cleaning explanation") {
                    questionValidating.explanation = questionValidating.explanation.trim()
                }
                worker("Cleaning answer options") {
                    questionValidating.answerOptions =
                        questionValidating.answerOptions.map { it.copy(answerText = it.answerText.trim()) }
                }
                validateIdNotEmpty("Checking for non-empty id")
                validateIdProperFormat("Checking id format")
                validateLockNotEmpty("Checking for non-empty lock")
                validateLockProperFormat("Checking lock format")
                validateTextNotEmpty("Checking that the text is not empty")
                validateTextHasContent("Character checking")
                validateAnswerNotEmpty("Checking that the answer is not empty")
                validateAnswerHasContent("Character checking")
                validateExplanationNotEmpty("Checking that the explanation is not empty")
                validateExplanationHasContent("Character checking")
                validateAnswerOptionsNotEmpty("Checking that the answerOptions is not empty")
                validateAnswerOptionNotEmpty("Checking that the answerOption is not empty")
                validateAnswerOptionHasContent("Character checking")

                finishQuestionValidation("Completing checks")
            }
        }

        operation("Delete question", QuizCommand.DELETE)
        {
            stubs("Processing stubs") {
                stubDeleteSuccess("Simulating successful processing", corSettings)
                stubValidationBadId("Simulating an id validation error")
                stubDbError("Simulating a database error")
                stubNoCase("Error: The requested stub is invalid")
            }
            validation {
                worker("Copy fields in questionValidating") { questionValidating = questionRequest.deepCopy() }
                worker("Cleaning id") {
                    questionValidating.id = QuizQuestionId(questionValidating.id.asString().trim())
                }
                worker("Cleaning lock") {
                    questionValidating.lock = QuizQuestionLock(questionValidating.lock.asString().trim())
                }
                validateIdNotEmpty("Checking for non-empty id")
                validateIdProperFormat("Checking id format")
                validateLockNotEmpty("Checking for non-empty lock")
                validateLockProperFormat("Checking lock format")

                finishQuestionValidation("Completing checks")
            }
        }

        operation("Search question", QuizCommand.SEARCH)
        {
            stubs("Processing stubs") {
                stubSearchSuccess("Simulating successful processing", corSettings)
                stubValidationBadId("Simulating an id validation error")
                stubDbError("Simulating a database error")
                stubNoCase("Error: The requested stub is invalid")
            }
            validation {
                worker("Copy fields in questionValidating") { questionValidating = questionRequest.deepCopy() }
                worker("Cleaning id") {
                    questionValidating.id = QuizQuestionId(questionValidating.id.asString().trim())
                }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishQuestionFilterValidation("Успешное завершение процедуры валидации")
            }
        }

    }.build()
}
