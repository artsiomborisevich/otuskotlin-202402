package dev.arborisevich.otuskotlin.kotlinwiz.common.repo.exceptions

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock

class RepoConcurrencyException(id: QuizQuestionId, expectedLock: QuizQuestionLock, actualLock: QuizQuestionLock?):
    RepoQuestionException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
