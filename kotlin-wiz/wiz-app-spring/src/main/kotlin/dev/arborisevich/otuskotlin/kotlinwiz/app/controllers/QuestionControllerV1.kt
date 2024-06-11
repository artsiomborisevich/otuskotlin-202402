package dev.arborisevich.otuskotlin.kotlinwiz.app.controllers

import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.fromTransport
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.toTransportQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.IResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionCreateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionDeleteResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionReadResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionSearchResponse
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateRequest
import dev.arborisevich.otuskotlin.kotlinwiz.api.v1.models.QuestionUpdateResponse
import dev.arborisevich.otuskotlin.kotlinwiz.app.common.controllerHelper
import dev.arborisevich.otuskotlin.kotlinwiz.app.config.QuizAppSettings
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/question")
class QuestionControllerV1(
    private val appSettings: QuizAppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: QuestionCreateRequest): QuestionCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: QuestionReadRequest): QuestionReadResponse =
        process(appSettings, request = request, this::class, "read")

    @PostMapping("update")
    suspend fun  update(@RequestBody request: QuestionUpdateRequest): QuestionUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: QuestionDeleteRequest): QuestionDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: QuestionSearchRequest): QuestionSearchResponse =
        process(appSettings, request = request, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: QuizAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportQuestion() as R },
            clazz,
            logId,
        )
    }
}
