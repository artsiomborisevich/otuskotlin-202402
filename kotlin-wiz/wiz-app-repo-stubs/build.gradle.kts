plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(libs.coroutines.core)
    implementation(kotlin("stdlib"))
    implementation(project(":wiz-common"))
    implementation(project(":wiz-stubs"))

    testImplementation(kotlin("test-junit"))
}