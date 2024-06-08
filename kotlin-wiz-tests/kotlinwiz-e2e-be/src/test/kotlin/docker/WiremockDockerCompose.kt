package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.docker

import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.docker.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock_1", 8080, "docker-compose-wiremock.yml"
)
