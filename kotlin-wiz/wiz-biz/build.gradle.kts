plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.datatype)
    implementation(project(":wiz-common"))
    implementation(project(":wiz-stubs"))
    implementation(libs.cor)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.coroutines.test)
}



