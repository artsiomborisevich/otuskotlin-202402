plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":wiz-common"))
    testImplementation(kotlin("test-junit"))
}
