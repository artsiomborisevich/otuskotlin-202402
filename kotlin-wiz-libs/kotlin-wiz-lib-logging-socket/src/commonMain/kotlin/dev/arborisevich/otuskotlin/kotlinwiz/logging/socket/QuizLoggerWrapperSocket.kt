package dev.arborisevich.otuskotlin.kotlinwiz.logging.socket

import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.IQuizLogWrapper
import dev.arborisevich.otuskotlin.kotlinwiz.logging.common.LogLevel
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

@ExperimentalStdlibApi
class QuizLoggerWrapperSocket(
    override val loggerId: String,
    private val host: String = "127.0.0.1",
    private val port: Int = 9002,
    private val emitToStdout: Boolean = true,
    bufferSize: Int = 16,
    overflowPolicy: BufferOverflow = BufferOverflow.SUSPEND,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default + CoroutineName("Logging")),
) : IQuizLogWrapper {
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private val sf = MutableSharedFlow<LogData>(
        extraBufferCapacity = bufferSize,
        onBufferOverflow = overflowPolicy,
    )
    private val isActive: AtomicBoolean = atomic(true)
    val isReady: AtomicBoolean = atomic(false)
    private val jsonSerializer = Json {
        encodeDefaults = true
    }

    private val job = scope.launch { handleLogs() }

    private suspend fun handleLogs() {
        while (isActive.value) {
            try {

                aSocket(selectorManager).tcp().connect(host, port).use { socket ->
                    socket.openWriteChannel().use {
                        sf
                            .onSubscription { isReady.value = true }
                            .collect {
                                val json = jsonSerializer.encodeToString(LogData.serializer(), it)
                                if (emitToStdout) println(json)
                                writeStringUtf8(json + "\n")
                                flush()
                            }
                    }
                }
            } catch (e: Throwable) {
                println("Error connecting log socket: $e")
                e.printStackTrace()
                delay(300)
            }
        }
    }

    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        data: Any?,
        objs: Map<String, Any>?
    ) {
        runBlocking {
            sf.emit(
                LogData(
                    level = level,
                    message = msg,
                )
            )
        }
    }

    override fun close() {
        isActive.value = false
        isReady.value = false
        job.cancel(message = "Finishing")
    }
}
