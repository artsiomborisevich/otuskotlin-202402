plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "dev.arborisevich.otuskotlin.kotlinwiz"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

tasks {
    create("check") {
        group = "verification"
        dependsOn(gradle.includedBuild("kotlin-wiz").task(":check"))
    }

    create("build") {
        group = "build"
        dependsOn(gradle.includedBuild("kotlin-wiz").task(":build"))
    }
}
