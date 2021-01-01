import kotlin.String

/**
 * Generated by [gradle-kotlin-dsl-libs](https://github.com/jmfayard/gradle-kotlin-dsl-libs)
 *
 * Run again
 *   `$ ./gradlew syncLibs`
 * to update this file */
object Libs {
    /**
     * [core-testing website](https://developer.android.com/topic/libraries/architecture/index.html) */
    const val core_testing: String = "android.arch.core:core-testing:" + Versions.core_testing

    /**
     * [core-testing website](https://developer.android.com/topic/libraries/architecture/index.html) */
    const val kotlinx_coroutines_test: String = "org.jetbrains.kotlinx:kotlinx-coroutines-test:" + Versions.kotlinx_coroutines_android

    /**
     * [extensions website](https://developer.android.com/topic/libraries/architecture/index.html) */
    const val android_arch_lifecycle_extensions: String =
            "android.arch.lifecycle:extensions:" + Versions.android_arch_lifecycle_extensions

    /**
     * [appcompat website](https://developer.android.com/jetpack/androidx) */
    const val appcompat: String = "androidx.appcompat:appcompat:" + Versions.appcompat

    /**
     * [constraintlayout website](http://tools.android.com) */
    const val constraintlayout: String =
            "androidx.constraintlayout:constraintlayout:" + Versions.constraintlayout

    /**
     * [core-ktx website](http://developer.android.com/tools/extras/support-library.html) */
    const val core_ktx: String = "androidx.core:core-ktx:" + Versions.core_ktx

    /**
     * [espresso-core website](https://developer.android.com/testing) */
    const val espresso_core: String =
            "androidx.test.espresso:espresso-core:" + Versions.espresso_core

    /**
     * [rules website](https://developer.android.com/testing) */
    const val androidx_test_rules: String = "androidx.test:rules:" + Versions.androidx_test

    /**
     * [runner website](https://developer.android.com/testing) */
    const val androidx_test_runner: String = "androidx.test:runner:" + Versions.androidx_test


    /**
     * [espresso-contrib website](https://developer.android.com/testing) */
    const val espresso_contrib: String =
        "androidx.test.espresso:espresso-contrib:" + Versions.androidx_test_espresso

    /**
     * [junit website](https://developer.android.com/testing) */
    const val androidx_test_ext_junit: String =
            "androidx.test.ext:junit:" + Versions.androidx_test_ext_junit

    /**
     * [material website](http://developer.android.com/tools/extras/support-library.html) */
    const val material: String = "com.google.android.material:material:" + Versions.material

    /**
     * [retrofit2-kotlin-coroutines-adapter website](https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter/) */
    const val retrofit2_kotlin_coroutines_adapter: String =
            "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:" + Versions.retrofit2_kotlin_coroutines_adapter

    /**
     * [logging-interceptor website](https://github.com/square/okhttp) */
    const val logging_interceptor: String =
            "com.squareup.okhttp3:logging-interceptor:" + Versions.com_squareup_okhttp3

    /**
     * [okhttp website](https://github.com/square/okhttp) */
    const val okhttp: String = "com.squareup.okhttp3:okhttp:" + Versions.com_squareup_okhttp3

    /**
     * [converter-gson website](https://github.com/square/retrofit/) */
    const val converter_gson: String =
            "com.google.code.gson:gson:" + Versions.gson_version

    /**
     * [retrofit website](https://github.com/square/retrofit/) */
    const val retrofit: String =
            "com.squareup.retrofit2:retrofit:" + Versions.com_squareup_retrofit2

    const val arrow_core: String = "io.arrow-kt:arrow-core:" + Versions.arrow_core

    /**
     * [mockk-android website](http://mockk.io) */
    const val mockk_android: String = "io.mockk:mockk-android:" + Versions.io_mockk

    /**
     * [mockk website](http://mockk.io) */
    const val mockk: String = "io.mockk:mockk:" + Versions.io_mockk

    const val jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin: String =
            "jmfayard.github.io.gradle-kotlin-dsl-libs:jmfayard.github.io.gradle-kotlin-dsl-libs.gradle.plugin:" + Versions.jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin

    /**
     * [junit website](http://junit.org) */
    const val junit_junit: String = "junit:junit:" + Versions.junit_junit

    /**
     * [kotlin-android-extensions-runtime website](https://kotlinlang.org/) */
    const val kotlin_android_extensions_runtime: String =
            "org.jetbrains.kotlin:kotlin-android-extensions-runtime:" + Versions.kotlin_android_extensions_runtime

    /**
     * [kotlin-android-extensions website](https://kotlinlang.org/) */
    const val kotlin_android_extensions: String =
            "org.jetbrains.kotlin:kotlin-android-extensions:" + Versions.kotlin_android_extensions

    /**
     * [kotlin-annotation-processing-gradle website](https://kotlinlang.org/) */
    const val kotlin_annotation_processing_gradle: String =
            "org.jetbrains.kotlin:kotlin-annotation-processing-gradle:" + Versions.kotlin_annotation_processing_gradle

    /**
     * [kotlin-gradle-plugin website](https://kotlinlang.org/) */
    const val kotlin_gradle_plugin: String =
            "org.jetbrains.kotlin:kotlin-gradle-plugin:" + Versions.kotlin_gradle_plugin

    /**
     * [kotlin-stdlib-jdk7 website](https://kotlinlang.org/) */
    const val kotlin_stdlib_jdk7: String =
            "org.jetbrains.kotlin:kotlin-stdlib-jdk7:" + Versions.kotlin_stdlib_jdk7

    const val kotlin_reflect: String =
        "org.jetbrains.kotlin:kotlin-reflect:" + Versions.kotlin_stdlib_jdk7

    /**
     * [kotlinx-coroutines-android website](https://github.com/Kotlin/kotlinx.coroutines) */
    const val kotlinx_coroutines_android: String =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:" + Versions.kotlinx_coroutines_android

    const val koin_android_viewmodel: String =
            "org.koin:koin-android-viewmodel:" + Versions.org_koin

    const val koin_core: String = "org.koin:koin-core:" + Versions.org_koin

    const val koin_test: String = "org.koin:koin-test:" + Versions.org_koin

    const val scarlet: String = "com.tinder.scarlet:scarlet:" + Versions.scarlet

    const val scarlet_test_utils: String = "com.tinder.scarlet:test-utils:" + Versions.scarlet

    const val scarlet_websocket_mockwebserver: String = "com.tinder.scarlet:websocket-mockwebserver:" + Versions.scarlet

    const val scarlet_websocket_scarlet_core_internal: String = "com.tinder.scarlet:scarlet-core-internal:" + Versions.scarlet

    const val scarlet_websocket_okhttp: String = "com.tinder.scarlet:websocket-okhttp:" + Versions.scarlet

    const val scarlet_stream_adapter_coroutines: String = "com.tinder.scarlet:stream-adapter-coroutines:" + Versions.scarlet

    const val scarlet_stream_adapter_rxjava: String = "com.tinder.scarlet:stream-adapter-rxjava2:" + Versions.scarlet

    const val scarlet_lifecycle_android: String = "com.tinder.scarlet:lifecycle-android:" + Versions.scarlet

    const val scarlet_message_adapter_moshi: String = "com.tinder.scarlet:message-adapter-moshi:" + Versions.scarlet

    const val lifecycle_viewmodel: String = "androidx.lifecycle:lifecycle-viewmodel-ktx:" + Versions.lifecycle_viewmodel

    const val lifecycle_runtime_ktx: String = "androidx.lifecycle:lifecycle-runtime-ktx:" + Versions.lifecycle_runtime_ktx

}


