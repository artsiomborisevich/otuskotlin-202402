package dev.arborisevich.otuskotlin.kotlinwiz.app.kafka

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.apiV1RequestDeserialize
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.apiV1ResponseSerialize
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.fromTransport
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.QuizContext

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: QuizContext): String {
        val response: IResponse = source.toTransportQuestion()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: QuizContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
