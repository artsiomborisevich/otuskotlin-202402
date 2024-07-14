package dev.arborisevich.otuskotlin.kotlinwiz.common.helpers

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.LogLevel

fun Throwable.asQuizError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = QuizError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun QuizContext.addError(vararg error: QuizError) = errors.addAll(error)
inline fun QuizContext.addErrors(error: Collection<QuizError>) = errors.addAll(error)

inline fun QuizContext.fail(error: QuizError) {
    addError(error)
    state = QuizState.FAILING
}

inline fun QuizContext.fail(errors: Collection<QuizError>) {
    addErrors(errors)
    state = QuizState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Code describing the error. Must not include a field name or validation indication.
     * Example: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = QuizError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = QuizError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
