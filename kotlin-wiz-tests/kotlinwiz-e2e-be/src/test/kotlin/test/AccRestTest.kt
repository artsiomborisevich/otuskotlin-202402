package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.test

import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.docker.SpringDockerCompose
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.docker.WiremockDockerCompose
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.BaseFunSpec
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.client.RestClient
import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.docker.DockerCompose
import io.kotest.core.annotation.Ignored


@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, "rest ")
})


class AccRestWiremockTest : AccRestTestBase(WiremockDockerCompose)

@Ignored //TODO adjust when logic would be created
class AccRestSpringTest : AccRestTestBase(SpringDockerCompose)