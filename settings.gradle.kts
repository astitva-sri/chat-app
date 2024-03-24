pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://storage.zego.im/maven")
        }
        maven {
            url = uri("https://www.jitpack.io")
        }
    }
}

rootProject.name = "Chat Things Up"
include(":app")
