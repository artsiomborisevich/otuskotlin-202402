plugins {
    id("build-jvm")
}

dependencies {
    implementation(project(":wiz-common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)

    testImplementation(kotlin("test-junit"))
}