package dev.arborisevich.otuskotlin.kotlinwiz.logging.jvm

import ch.qos.logback.classic.Logger
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.IQuizLogWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun quizLoggerLogback(logger: Logger): IQuizLogWrapper = QuizLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun quizLoggerLogback(clazz: KClass<*>): IQuizLogWrapper = quizLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun quizLoggerLogback(loggerId: String): IQuizLogWrapper = quizLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
