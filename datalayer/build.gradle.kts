import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// first option
plugins {
    id("com.android.library")
    id("io.github.dryrum.replace-in-file")
    id("org.jetbrains.dokka")
    kotlin("android")
}
// second option
apply(plugin = "kotlin-android-extensions")
apply(from = rootDir.path + "/buildfile/ktlint_utils.gradle")
apply(from = rootDir.path + "/buildfile/publish-mavencentral.gradle")

group = project.property("GROUP_ID") as String
version = rootProject.extra.get("version_name") as String

android {
    compileSdkVersion(29)

    defaultConfig {
        targetSdkVersion(29)
        minSdkVersion(23)
        versionCode = 1
        versionName = ""
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "SOCKET_URL", "\"wss://ws-feed.gdax.com\"")
            buildConfigField("String", "URL_PRODUCTS", "\"https://api.gdax.com/products\"")
        }
        getByName("release") {
            isMinifyEnabled = false
            buildConfigField("String", "SOCKET_URL", "\"wss://ws-feed.gdax.com\"")
            buildConfigField("String", "URL_PRODUCTS", "\"https://api.gdax.com/products\"")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType(KotlinCompile::class.java) {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {

    api(project(":okhttp-socket-ext"))

    implementation(Libs.kotlin_stdlib_jdk7)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.appcompat)
    implementation(Libs.core_ktx)
    implementation(Libs.constraintlayout)
    implementation(Libs.kotlinx_coroutines_android)
    implementation(Libs.material)

    implementation(Libs.arrow_core)

    // di
    implementation(Libs.koin_core)
    implementation(Libs.koin_android_viewmodel)

    // web socket
    implementation(Libs.scarlet)
    implementation(Libs.scarlet_websocket_okhttp)
    implementation(Libs.scarlet_stream_adapter_coroutines)
    implementation(Libs.scarlet_stream_adapter_rxjava)
    implementation(Libs.scarlet_message_adapter_moshi)
    implementation(Libs.scarlet_lifecycle_android)

    implementation(Libs.converter_gson)
    api(Libs.okhttp)

    implementation(Libs.lifecycle_viewmodel)
    implementation(Libs.lifecycle_runtime_ktx)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    androidTestImplementation(Libs.koin_test)
    androidTestImplementation(Libs.androidx_test_ext_junit)

    androidTestImplementation(Libs.androidx_test_runner)
    androidTestImplementation(Libs.androidx_test_rules)
    androidTestImplementation(Libs.espresso_core)
    androidTestImplementation(Libs.espresso_contrib)
    androidTestImplementation(Libs.scarlet_websocket_scarlet_core_internal)
    androidTestImplementation(Libs.mockk_android)

    testImplementation(Libs.kotlinx_coroutines_test)
    testImplementation(Libs.scarlet_websocket_scarlet_core_internal)
    testImplementation(Libs.junit_junit)
    testImplementation(Libs.mockk)
    testImplementation(Libs.core_testing)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)

    //RXJava2
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.19")

}

replaceInFile{
    docs{
        create("doc"){
            path = rootDir.path + "/README.md"
            find = "io.github.ciriti:okhttp-socket-ext:(\\d)+\\.(\\d)+\\.(\\d)+"
            replaceWith = "io.github.ciriti:okhttp-socket-ext:$version"
        }
        create("doc1"){
            path = rootDir.path + "/README.md"
            find = "io.github.ciriti:gdax-data:1.3.7"
            replaceWith = "io.github.ciriti:gdax-data:$version"
        }
    }
}