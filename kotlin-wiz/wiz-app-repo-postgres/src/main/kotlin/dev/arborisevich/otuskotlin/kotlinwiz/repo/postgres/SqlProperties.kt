package dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "quiz-pass",
    val database: String = "quiz_questions",
    val schema: String = "public",
    val questionTable: String = "quiz_item",
    val answerOptionTable: String = "answer_option",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
