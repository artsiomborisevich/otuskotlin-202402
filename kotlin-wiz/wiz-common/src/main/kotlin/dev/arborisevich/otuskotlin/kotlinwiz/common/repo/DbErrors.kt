package dev.arborisevich.otuskotlin.kotlinwiz.common.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorSystem
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.exceptions.RepoConcurrencyException
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: QuizQuestionId) = DbQuestionResponseErr(
    QuizError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbQuestionResponseErr(
    QuizError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldQuestion: QuizQuestion,
    expectedLock: QuizQuestionLock,
    exception: Exception = RepoConcurrencyException(
        id = oldQuestion.id,
        expectedLock = expectedLock,
        actualLock = oldQuestion.lock,
    ),
) = DbQuestionResponseErrWithData(
    question = oldQuestion,
    err = QuizError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldQuestion.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: QuizQuestionId) = DbQuestionResponseErr(
    QuizError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbQuestionResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
