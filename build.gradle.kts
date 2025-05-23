// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    /**
     * Use `apply false` in the top-level build.gradle file to add a Gradle
     * plugin as a build dependency but not apply it to the current (root)
     * project. Don't use `apply false` in sub-projects. For more information,
     * see Applying external plugins with same version to subprojects.
     */

    id("com.android.application") version "7.4.2" apply false

    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
}

extra["kotlinVersion"] = "1.6.21"
extra["archLifecycleVersion"] = "1.1.1"

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
