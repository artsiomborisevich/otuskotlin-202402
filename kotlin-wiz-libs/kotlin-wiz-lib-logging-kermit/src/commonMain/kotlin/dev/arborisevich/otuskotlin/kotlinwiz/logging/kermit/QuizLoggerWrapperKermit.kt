package dev.arborisevich.otuskotlin.kotlinwiz.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.IQuizLogWrapper
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.LogLevel

class QuizLoggerWrapperKermit(
    val logger: Logger,
    override val loggerId: String
    ) : IQuizLogWrapper {

    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        data: Any?,
        objs: Map<String,Any>?
    ) {
        logger.log(
            severity = level.toKermit(),
            tag = marker,
            throwable = e,
            message = formatMessage(msg, data, objs),
        )
    }

    private fun LogLevel.toKermit() = when(this) {
        LogLevel.ERROR -> Severity.Error
        LogLevel.WARN -> Severity.Warn
        LogLevel.INFO -> Severity.Info
        LogLevel.DEBUG -> Severity.Debug
        LogLevel.TRACE -> Severity.Verbose
    }

    private inline fun formatMessage(
        msg: String = "",
        data: Any? = null,
        objs: Map<String,Any>? = null
    ): String {
        var message = msg
        data?.let {
            message += "\n" + data.toString()
        }
        objs?.forEach {
            message += "\n" + it.toString()
        }
        return message
    }

}
