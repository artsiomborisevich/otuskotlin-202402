plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    api(libs.kotlinx.datetime)
    api("dev.arborisevich.otuskotlin.kotlinwiz.libs:kotlin-wiz-lib-logging-common")
}