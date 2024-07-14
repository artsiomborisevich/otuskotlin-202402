plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    id("build-jvm")
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(project(":wiz-common"))
    implementation(project(":wiz-app-common"))
    implementation("dev.arborisevich.otuskotlin.kotlinwiz.libs:kotlin-wiz-lib-logging-logback")

    // v1 api
    implementation(project(":wiz-api-v1-jackson"))
    implementation(project(":wiz-api-v1-mappers"))

    // biz
    implementation(project(":wiz-biz"))

    // DB
    implementation(project(":wiz-app-repo-inmemory"))
    implementation(project(":wiz-app-repo-stubs"))
    implementation(project(":wiz-app-repo-common"))
    implementation(project(":wiz-app-repo-postgres"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.mockk)
    testImplementation(libs.mockito.kotlin)
    testImplementation(project(":wiz-app-repo-stubs"))
    testImplementation(project(":wiz-stubs"))
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
