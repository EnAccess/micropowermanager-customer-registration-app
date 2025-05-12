// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "4.2.2" apply false

    id("org.jetbrains.kotlin.android") version "1.4.32" apply false
}

extra["kotlin_version"] = "1.4.32"
extra["roomVersion"] = "1.1.1"
extra["archLifecycleVersion"] = "1.1.1"

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
