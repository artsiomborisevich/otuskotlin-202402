plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":wiz-api-v1-jackson"))
    implementation(project(":wiz-common"))

    testImplementation(kotlin("test-junit"))
}
