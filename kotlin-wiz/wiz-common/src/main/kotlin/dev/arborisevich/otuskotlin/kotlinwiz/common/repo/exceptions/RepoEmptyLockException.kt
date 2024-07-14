package dev.arborisevich.otuskotlin.kotlinwiz.common.repo.exceptions

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId

class RepoEmptyLockException(id: QuizQuestionId): RepoQuestionException(
    id,
    "Lock is empty in DB"
)
