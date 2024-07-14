package dev.arborisevich.otuskotlin.kotlinwiz.repo


import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.*

class QuestionRepoInMemoryCreateTest : RepoQuestionCreateTest() {
    override val repo = QuestionRepoInitialized(
        QuestionRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class QuestionRepoInMemoryDeleteTest : RepoQuestionDeleteTest() {
    override val repo = QuestionRepoInitialized(
        QuestionRepoInMemory(),
        initObjects = initObjects,
    )
}

class QuestionRepoInMemoryReadTest : RepoQuestionReadTest() {
    override val repo = QuestionRepoInitialized(
        QuestionRepoInMemory(),
        initObjects = initObjects,
    )
}

class QuestionRepoInMemorySearchTest : RepoQuestionSearchTest() {
    override val repo = QuestionRepoInitialized(
        QuestionRepoInMemory(),
        initObjects = initObjects,
    )
}

class QuestionRepoInMemoryUpdateTest : RepoQuestionUpdateTest() {
    override val repo = QuestionRepoInitialized(
        QuestionRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
