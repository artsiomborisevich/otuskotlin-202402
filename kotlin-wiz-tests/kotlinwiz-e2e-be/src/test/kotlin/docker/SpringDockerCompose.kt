package dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.docker

import dev.arborisevich.otuskotlin.kotlinwiz.e2e.be.fixture.docker.AbstractDockerCompose

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring_1", 8080, "docker-compose-spring.yml"
)
