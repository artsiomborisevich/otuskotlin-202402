package dev.arborisevich.otuskotlin.kotlinwiz.app.common

import dev.arborisevich.otuskotlin.kotlinwiz.api.log1.mapper.toLog
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext
import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.asQuizError
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizState
import kotlinx.datetime.Clock
import kotlin.reflect.KClass

suspend inline fun <T> IQuizAppSettings.controllerHelper(
    crossinline getRequest: suspend QuizContext.() -> Unit,
    crossinline toResponse: suspend QuizContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {

    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = QuizContext(
        timeStart = Clock.System.now(),
    )

    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )

        ctx.state = QuizState.FAILING
        ctx.errors.add(e.asQuizError())
        processor.exec(ctx)

        if (ctx.command == QuizCommand.NONE) {
            ctx.command = QuizCommand.READ
        }
        ctx.toResponse()
    }
}
