package dev.arborisevich.otuskotlin.kotlinwiz.core

interface ICorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}
