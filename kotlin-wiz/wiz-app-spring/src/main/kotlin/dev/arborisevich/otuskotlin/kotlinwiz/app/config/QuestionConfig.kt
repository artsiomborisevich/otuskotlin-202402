package dev.arborisevich.otuskotlin.kotlinwiz.app.config

import dev.arborisevich.otuskotlin.kotlinwiz.biz.QuizQuestionProcessor
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizCoreSettings
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.QuizLoggerProvider
import dev.arborisevich.otuskotlin.kotlinwiz.logging.jvm.quizLoggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class QuestionConfig {

    @Bean
    fun processor(corSettings: QuizCoreSettings) = QuizQuestionProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): QuizLoggerProvider = QuizLoggerProvider { quizLoggerLogback(it) }

    @Bean
    fun corSettings(): QuizCoreSettings = QuizCoreSettings(
        loggerProvider = loggerProvider(),
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
