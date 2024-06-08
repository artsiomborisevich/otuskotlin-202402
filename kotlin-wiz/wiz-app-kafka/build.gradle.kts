plugins {
    application
    id("build-jvm")
}

application {
    mainClass.set("dev.arborisevich.otuskotlin.kotlinwiz.app.kafka.MainKt")
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("dev.arborisevich.otuskotlin.kotlinwiz.libs:kotlin-wiz-lib-logging-logback")

    implementation(project(":wiz-app-common"))

    // transport models
    implementation(project(":wiz-common"))
    implementation(project(":wiz-api-v1-jackson"))
    implementation(project(":wiz-api-v1-mappers"))
    // logic
    implementation(project(":wiz-biz"))

    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

