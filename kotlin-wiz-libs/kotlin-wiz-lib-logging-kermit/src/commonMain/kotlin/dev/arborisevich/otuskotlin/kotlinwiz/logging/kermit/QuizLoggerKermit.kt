package dev.arborisevich.otuskotlin.kotlinwiz.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.IQuizLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun quizLoggerKermit(loggerId: String): IQuizLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return QuizLoggerWrapperKermit(
        logger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun quizLoggerKermit(cls: KClass<*>): IQuizLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return QuizLoggerWrapperKermit(
        logger = logger,
        loggerId = cls.qualifiedName?: "",
    )
}
