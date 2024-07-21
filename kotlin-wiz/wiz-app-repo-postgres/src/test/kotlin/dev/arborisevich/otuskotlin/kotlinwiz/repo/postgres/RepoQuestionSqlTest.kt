package dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres

import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.repo.IRepoQuestionInitializable
import dev.arborisevich.otuskotlin.kotlinwiz.repo.QuestionRepoInitialized
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.RepoQuestionCreateTest
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.RepoQuestionDeleteTest
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.RepoQuestionReadTest
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.RepoQuestionSearchTest
import dev.arborisevich.otuskotlin.kotlinwiz.repo.tests.RepoQuestionUpdateTest
import kotlin.test.AfterTest

private fun IRepoQuestion.clear() {
    val pgRepo = (this as QuestionRepoInitialized).repo as RepoQuestionSql
    pgRepo.clear()
}

class RepoQuestionSqlTestt : RepoQuestionCreateTest() {
    override val repo: IRepoQuestionInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { uuidNew.asString() },
    )

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoQuestionSQLReadTest : RepoQuestionReadTest() {
    override val repo: IRepoQuestion = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoQuestionSQLUpdateTest : RepoQuestionUpdateTest() {
    override val repo: IRepoQuestion = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )

    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoQuestionSQLDeleteTest : RepoQuestionDeleteTest() {
    override val repo: IRepoQuestion = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoQuestionSQLSearchTest : RepoQuestionSearchTest() {
    override val repo: IRepoQuestion = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}