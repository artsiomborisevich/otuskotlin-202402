package dev.arborisevich.otuskotlin.kotlinwiz.app.config

import dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres.SqlProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "psql")
@Configuration
data class QuestionConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "quiz-pass",
    var database: String = "quiz_questions",
    var schema: String = "public",
    var table: String = "quiz_item",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        questionTable = table,
    )
}
