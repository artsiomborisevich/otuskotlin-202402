package dev.arborisevich.otuskotlin.kotlinwiz.api.v1

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateObject
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDebug
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugMode
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionRequestDebugStubs
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RequestV1SerializationTest {

    private val request = QuestionCreateRequest(
        debug = QuestionDebug(
            mode = QuestionRequestDebugMode.STUB,
            stub = QuestionRequestDebugStubs.BAD_TEXT
        ),
        question = QuestionCreateObject(
            text = "Question text",
            answer = "Question answer",
            answerOptions = listOf(AnswerOption("1","first option")),
            explanation = "Question explanation",
            level = QuestionLevel.BEGINNER,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        println(json)

        assertContains(json, Regex("\"requestType\":\\s*\"create\""))

        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badText\""))

        assertContains(json, Regex("\"text\":\\s*\"Question text\""))
        assertContains(json, Regex("\"answer\":\\s*\"Question answer\""))
        assertContains(json, Regex("\"explanation\":\\s*\"Question explanation\""))
        assertContains(json, Regex("\"level\":\\s*\"beginner\""))

        assertContainsReq(json, "\"answerOptions\":[{\"id\":\"1\",\"text\":\"first option\"}]")
    }

    private fun assertContainsReq(json: String, toFind: String) {
        assertTrue(json.contains(toFind), "String not found in JSON: $toFind")
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        println(json)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as QuestionCreateRequest

        val reqWithType = request.copy(requestType = "create")
        assertEquals(reqWithType, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"question": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, QuestionCreateRequest::class.java)

        assertEquals(null, obj.question)
    }
}
