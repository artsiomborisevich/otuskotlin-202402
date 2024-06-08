plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    api(libs.kotlinx.datetime)
}