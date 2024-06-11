package dev.arborisevich.otuskotlin.kotlinwiz.app.kafka

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.apiV1RequestSerialize
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.apiV1ResponseDeserialize
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDebug
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugMode
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugStubs
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        QuestionCreateRequest(
                            question = QuestionCreateObject(
                                text = "text",
                                answer = "answer",
                                answerOptions = listOf(
                                    AnswerOption("1","answerOption1"),
                                    AnswerOption("2","answerOption2")
                                ),
                                explanation = "explanation",
                                level = QuestionLevel.ADVANCED,
                            ),
                            debug = QuestionDebug(
                                mode = QuestionRequestDebugMode.STUB,
                                stub = QuestionRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<QuestionCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("What is the difference between var and val in Kotlin?", result.question?.text)
    }

    companion object {
        const val PARTITION = 0
    }
}


