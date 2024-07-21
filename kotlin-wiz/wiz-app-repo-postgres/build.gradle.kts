
import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerInspectContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStopContainer
import com.bmuschko.gradle.docker.tasks.container.DockerWaitContainer
import com.bmuschko.gradle.docker.tasks.image.DockerInspectImage
import com.bmuschko.gradle.docker.tasks.image.DockerPullImage
import com.github.dockerjava.api.command.InspectContainerResponse
import com.github.dockerjava.api.model.ExposedPort
import java.util.concurrent.atomic.AtomicBoolean

plugins {
    id("build-jvm")
    alias(libs.plugins.liquibase)
    alias(libs.plugins.muschko.remote)
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)
    implementation(libs.db.postgres)
//    implementation(libs.db.hikari)
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.bundles.exposed)

    implementation(project(":wiz-common"))
    implementation(project(":wiz-app-repo-common"))

    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.picocli)
    liquibaseRuntime(libs.liquibase.snakeyml)
    liquibaseRuntime(libs.db.postgres)

    testImplementation(project(":wiz-app-repo-tests"))
    testImplementation(kotlin("test-junit"))
}

var pgPort = 5432
val taskGroup = "pgContainer"
val pgDbName = "quiz_questions"
val pgUsername = "postgres"
val pgPassword = "quiz-pass"
val containerStarted = AtomicBoolean(false)

tasks {
    val postgresImage = "postgres:latest"

    // Task to check if the image exists locally
    val checkImageExists by creating(DockerInspectImage::class) {
        group = taskGroup
        imageId.set(postgresImage)
        onError {
            // If the image is not found, set a flag to false
            project.ext.set("imageExists", false)
        }
        onNext {
            // If the image is found, set a flag to true
            project.ext.set("imageExists", true)
        }
    }

    val pullImage by creating(DockerPullImage::class) {
        group = taskGroup
        image.set(postgresImage)
        dependsOn(checkImageExists)
        onlyIf {
            !project.ext.has("imageExists") || project.ext.get("imageExists") == false
        }
    }

    val dbContainer by creating(DockerCreateContainer::class) {
        group = taskGroup
        dependsOn(checkImageExists, pullImage)
        targetImageId(pullImage.image)
        withEnvVar("POSTGRES_PASSWORD", pgPassword)
        withEnvVar("POSTGRES_USER", pgUsername)
        withEnvVar("POSTGRES_DB", pgDbName)
        healthCheck.cmd("pg_isready")

        hostConfig.portBindings.set(listOf(":5432"))
        exposePorts("tcp", listOf(5432))
        hostConfig.autoRemove.set(true)
    }

    val stopPg by creating(DockerStopContainer::class) {
        group = taskGroup
        targetContainerId(dbContainer.containerId)
    }

    val startPg by creating(DockerStartContainer::class) {
        group = taskGroup
        dependsOn(dbContainer)
        targetContainerId(dbContainer.containerId)
        finalizedBy(stopPg)
    }

    val inspectPg by creating(DockerInspectContainer::class) {
        group = taskGroup
        dependsOn(startPg)
        finalizedBy(stopPg)
        targetContainerId(dbContainer.containerId)
        doFirst {
            println("Inspecting container ${dbContainer.containerId.get()}")
        }
        doLast {
            // Adding a delay to ensure the container is ready
            Thread.sleep(10000) // 10 seconds delay
        }
        onNext(
            object : Action<InspectContainerResponse> {
                override fun execute(container: InspectContainerResponse) {
                    println("Container State: ${container.state}")
                    pgPort = container.networkSettings.ports.bindings[ExposedPort.tcp(5432)]
                        ?.first()
                        ?.hostPortSpec
                        ?.toIntOrNull()
                        ?: throw Exception("Postgres port is not found in container")
                }
            }
        )
    }

    val liquibaseUpdate = getByName("update") {
        group = taskGroup
        dependsOn(inspectPg)
        finalizedBy(stopPg)
        doFirst {
            println("waiting for a while ${System.currentTimeMillis()/1000000}")
            Thread.sleep(30000)
            println("LQB: \"jdbc:postgresql://localhost:$pgPort/$pgDbName\" ${System.currentTimeMillis()/1000000}")
            liquibase {
                activities {
                    register("main") {
                        arguments = mapOf(
                            "logLevel" to "info",
                            "searchPath" to layout.projectDirectory.dir("migrations").asFile.toString(),
                            "changelogFile" to "changelog-v0.0.1.sql",
                            "url" to "jdbc:postgresql://localhost:$pgPort/$pgDbName",
                            "username" to pgUsername,
                            "password" to pgPassword,
                            "driver" to "org.postgresql.Driver"
                        )
                    }
                }
            }
        }
    }

    val waitPg by creating(DockerWaitContainer::class) {
        group = taskGroup
        dependsOn(inspectPg)
        dependsOn(liquibaseUpdate)
        containerId.set(startPg.containerId)
        finalizedBy(stopPg)
        doFirst {
            println("PORT: $pgPort")
        }
    }

    withType(Test::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
}