plugins {
    id("build-jvm")
}


dependencies {
    api(libs.coroutines.core)
    api(libs.coroutines.test)
    api(kotlin("test-junit"))
    implementation(project(":wiz-common"))
    implementation(project(":wiz-app-repo-common"))
    implementation(kotlin("stdlib"))

    testImplementation(project(":wiz-stubs"))
}