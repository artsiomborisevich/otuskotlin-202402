package dev.arborisevich.otuskotlin.kotlinwiz.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class QuizLoggerProvider(
    private val provider: (String) -> IQuizLogWrapper = { IQuizLogWrapper.DEFAULT }
) {

    fun logger(loggerId: String): IQuizLogWrapper = provider(loggerId)

    fun logger(clazz: KClass<*>): IQuizLogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>): IQuizLogWrapper = provider(function.name)
}

