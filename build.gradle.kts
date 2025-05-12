// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id 'com.android.application' version '4.2.2' apply false

    id 'org.jetbrains.kotlin.android' version '1.4.32' apply false
}

ext {
    kotlin_version = '1.4.32'
    roomVersion = '1.1.1'
    archLifecycleVersion = '1.1.1'
}

tasks.register('clean', Delete) {
    delete rootProject.buildDir
}
