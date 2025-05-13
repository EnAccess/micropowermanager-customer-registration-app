plugins {
    id("com.android.application")

    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.inensus.android"
    compileSdk = 29

    defaultConfig {
        applicationId = "com.inensus.android"
        minSdk = 21
        targetSdk = 29
        versionCode = 1
        versionName = "1.3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file("release.keystore")
            storePassword = "556211"
            keyAlias = "release"
            keyPassword = "556211"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            resValue("string", "app_name", "Customer Registration-DEBUG")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isZipAlignEnabled = true
            isShrinkResources = true
            isDebuggable = false
            resValue("string", "app_name", "Customer Registration")
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kapt {
        mapDiagnosticLocations = true
    }

    // Setting this to `VERSION_11` yields:
    // error: cannot find symbol
    //                   + " Found:\n" + _existingCustomers);
    //                                   ^
    //   symbol:   method makeConcatWithConstants(Lookup,String,MethodType,String)
    //   location: interface StringConcatFactory
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    lint {
        textOutput = file("stdout")
        textReport = true
    }

    buildFeatures {
        viewBinding = true
    }
}

val kotlin_version: String by rootProject.extra
val koin_version = "2.2.3"
val room_version = "2.2.5"

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.preference:preference:1.1.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0")
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.7.1")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.squareup.okhttp3:logging-interceptor:4.3.0")
    implementation("com.squareup.okhttp3:okhttp:4.3.0")
    implementation("com.squareup.retrofit2:converter-gson:2.7.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.roughike:BottomBar:V2.3.1")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.7.1")
    implementation("com.jakewharton.rxrelay2:rxrelay:2.1.0")
    implementation("androidx.core:core-ktx:1.5.0-alpha02")

    // Room components
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-rxjava2:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")
    androidTestImplementation("androidx.room:room-testing:$room_version")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    kapt("androidx.lifecycle:lifecycle-compiler:2.2.0")

    // Koin
    implementation("io.insert-koin:koin-androidx-scope:$koin_version")
    implementation("io.insert-koin:koin-androidx-viewmodel:$koin_version")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-core-ext:$koin_version")

    // other
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.google.android.material:material:1.3.0-alpha02")

    implementation("com.github.amulyakhare:textdrawable:558677e")
}

// Temporary workaround to build on M1 Mac's
// Can be fixed by updating room to 2.4
configurations.all {
    resolutionStrategy {
        force("org.xerial:sqlite-jdbc:3.34.0")
    }
}

tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}
