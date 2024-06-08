package dev.arborisevich.otuskotlin.kotlinwiz.app.kafka

data class InputOutputTopics(

    /**
     * Topic for inbound messages
     */
    val input: String,
    /**
     * Topic for outbound messages
     */
    val output: String,
)
