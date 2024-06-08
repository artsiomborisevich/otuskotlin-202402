plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)

    // transport models
    implementation(project(":wiz-common"))
    implementation(project(":wiz-api-log1"))
    implementation(project(":wiz-api-v1-jackson"))

    implementation(projects.wizBiz)

    api(libs.kotlinx.datetime)

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
    testImplementation(project(":wiz-api-v1-mappers"))

}