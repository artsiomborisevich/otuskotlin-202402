plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("dev.arborisevich.otuskotlin.kotlinwiz:wiz-api-v1-jackson")

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

//    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
//    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
//    testImplementation("io.ktor:ktor-client-okhttp:$ktorVersion")
//    testImplementation("io.ktor:ktor-client-okhttp-jvm:$ktorVersion")
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-spring").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-ktor").task("publishImageToLocalRegistry"))
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-rabbit").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-kafka").task("dockerBuildImage"))
    }
}
