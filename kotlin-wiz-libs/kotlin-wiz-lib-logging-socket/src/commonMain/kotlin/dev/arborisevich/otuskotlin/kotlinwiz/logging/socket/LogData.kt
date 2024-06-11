package dev.arborisevich.otuskotlin.kotlinwiz.logging.socket

import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.LogLevel
import kotlinx.serialization.Serializable

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)
