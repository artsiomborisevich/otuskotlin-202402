package ru.otus.otuskotlin.marketplace.api.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.apiV1Mapper
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionResponseObject
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResponseV1SerializationTest {

    private val response = QuestionCreateResponse(
        question = QuestionResponseObject(
            text = "Question text",
            answer = "Question answer",
            answerOptions = listOf(AnswerOption(1,"first option")),
            explanation = "Question explanation",
            level = QuestionLevel.BEGINNER,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"text\":\\s*\"Question text\""))
        assertContains(json, Regex("\"answer\":\\s*\"Question answer\""))
        assertContains(json, Regex("\"explanation\":\\s*\"Question explanation\""))
        assertContains(json, Regex("\"level\":\\s*\"beginner\""))

        assertContainsReq(json, "\"answerOptions\":[{\"id\":1,\"text\":\"first option\"}]")
    }

    private fun assertContainsReq(json: String, toFind: String) {
        assertTrue(json.contains(toFind), "String not found in JSON: $toFind")
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as QuestionCreateResponse

        val resWithType = response.copy(responseType = "create")
        assertEquals(resWithType, obj)
    }
}
