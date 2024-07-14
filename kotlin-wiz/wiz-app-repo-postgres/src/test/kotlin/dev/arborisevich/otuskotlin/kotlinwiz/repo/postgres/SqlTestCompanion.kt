package dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres

import com.benasher44.uuid.uuid4
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.repo.IRepoQuestionInitializable
import dev.arborisevich.otuskotlin.kotlinwiz.repo.QuestionRepoInitialized

fun getEnv(name: String): String? = System.getenv(name)

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "quiz-pass"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<QuizQuestion> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): IRepoQuestionInitializable = QuestionRepoInitialized(
        repo =  RepoQuestionSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}