plugins {
    id("build-jvm")
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":wiz-common"))
    implementation(project(":wiz-app-repo-common"))

    testImplementation(project(":wiz-app-repo-tests"))
    testImplementation(kotlin("test-junit"))
}