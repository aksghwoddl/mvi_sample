pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        versionCatalogs {
            create("libs")
        }
    }
}

rootProject.name = "MviSampleApp"
include(":app")
include(":circuitapp")
include(":core")
include(":core:data")
include(":core:data-impl")
include(":core:domain")
include(":library")
include(":library:test")
include(":core:presenter")
include(":library:design-system")
