package dev.arborisevich.otuskotlin.kotlinwiz.app.config

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.QuizLoggerProvider
import dev.arborisevich.otuskotlin.kotlinwiz.logging.jvm.quizLoggerLogback
import dev.arborisevich.otuskotlin.kotlinwiz.repo.QuestionRepoInMemory
import dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres.RepoQuestionSql
import dev.arborisevich.otuskotlin.kotlinwiz.repository.inmemory.QuestionRepoStub
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuestionConfig(val postgresConfig: QuestionConfigPostgres) {

    val logger: Logger = LoggerFactory.getLogger(QuestionConfig::class.java)

    @Bean
    fun processor(corSettings: QuizCoreSettings) = QuizQuestionProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): QuizLoggerProvider = QuizLoggerProvider { quizLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoQuestion = QuestionRepoInMemory()

    @Bean
    fun prodRepo(): IRepoQuestion = RepoQuestionSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with ${this}")
    }

    @Bean
    fun stubRepo(): IRepoQuestion = QuestionRepoStub()

    @Bean
    fun corSettings(): QuizCoreSettings = QuizCoreSettings(
        loggerProvider = loggerProvider(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
    )

    @Bean
    fun appSettings(
        corSettings: QuizCoreSettings,
        processor: QuizQuestionProcessor,
    ) = QuizAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
