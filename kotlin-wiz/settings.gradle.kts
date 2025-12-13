rootProject.name = "kotlinwiz"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":wiz-api-v1-jackson")
include(":wiz-api-v1-mappers")
include(":wiz-api-log1")

include(":wiz-common")
include(":wiz-biz")
include(":wiz-stubs")

include(":wiz-app-common")
include(":wiz-app-spring")
include(":wiz-app-kafka")

include(":wiz-app-repo-common")
include(":wiz-app-repo-inmemory")
include(":wiz-app-repo-stubs")
include(":wiz-app-repo-tests")
include(":wiz-app-repo-postgres")
