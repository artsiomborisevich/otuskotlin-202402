rootProject.name = "kotlin-wiz-libs"

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

include(":kotlin-wiz-lib-logging-common")
include(":kotlin-wiz-lib-logging-kermit")
include(":kotlin-wiz-lib-logging-logback")
include(":kotlin-wiz-lib-logging-socket")

include(":kotlin-wiz-lib-cor")
