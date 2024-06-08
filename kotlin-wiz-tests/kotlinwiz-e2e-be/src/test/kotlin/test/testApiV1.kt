package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.AnswerOption
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchFilter
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateObject
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.client.Client
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1.createQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1.deleteQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1.readQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1.searchQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test.action.v1.updateQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.test.action.v1.someCreateQuestion
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

fun FunSpec.testApiV1(client: Client, prefix: String = "") {
    context("${prefix}v1") {
        test("Create Question ok") {
            client.createQuestion()
        }

        test("Read Question ok") {
            val created = client.createQuestion()
            client.readQuestion(created.id).asClue {
                it shouldBe created
            }
        }

        test("Update Question ok") {
            val created = client.createQuestion()

            val answerOption1 = AnswerOption(id = "1", text = "To create classes that are used solely for holding data")
            val answerOption2 = AnswerOption(id = "2", text = "To define classes with custom behavior and logic")
            val answerOption3 = AnswerOption(id = "3", text = "To define classes that can only be inherited from")
            val answerOption4 = AnswerOption(id = "4", text = "To create classes that do not allow any properties")

            val answerOptions = listOf(answerOption1, answerOption2, answerOption3, answerOption4)

            val updateQuestion = QuestionUpdateObject(
                id = created.id,
                lock = created.lock,
                text = "What is the primary purpose of a data class in Kotlin?",
                answer = "To create classes that are used solely for holding data",
                explanation = "In Kotlin, a data class is used to hold data and provides automatically generated functions such as equals(), hashCode(), toString(), and copy().",
                answerOptions = answerOptions,
                level = created.level,
            )
            client.updateQuestion(updateQuestion)
        }

        test("Delete Question ok") {
            val created = client.createQuestion()
            client.deleteQuestion(created)
        }

        test("Search Question ok") {
            val created1 = client.createQuestion(someCreateQuestion.copy(
                text = "What is the difference between var and val?")
            )
            val created2 = client.createQuestion(someCreateQuestion.copy(text = "What is the var in Kotlin?"))
            val created3 = client.createQuestion(someCreateQuestion.copy(
                text = "What is the primary purpose of a data class in Kotlin?")
            )

            withClue("Search var question") {
                val results = client.searchQuestion(search = QuestionSearchFilter(searchString = "var"))
                results shouldHaveSize 2
                results shouldExist { it.text?.contains("var") ?: false }
                results shouldExist { it.text == created2.text }
            }

            withClue("Search data class questions") {
                client.searchQuestion(search = QuestionSearchFilter(searchString = "data class"))
                    .shouldExistInOrder({ it.text == created3.text })
            }
        }
    }

}
