package dev.arborisevich.otuskotlin.kotlinwiz.app.kafka

import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext

interface IConsumerStrategy {

    fun topics(config: AppKafkaConfig): InputOutputTopics

    fun serialize(source: QuizContext): String

    fun deserialize(value: String, target: QuizContext)
}
