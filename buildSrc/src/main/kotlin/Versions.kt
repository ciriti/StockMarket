/**
 * Find which updates are available by running
 *     `$ ./gradlew syncLibs`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val core_testing: String = "1.1.1"

    const val android_arch_lifecycle_extensions: String = "1.1.1"

    const val appcompat: String = "1.1.0"

    const val constraintlayout: String = "1.1.3" // exceed the version found: 1.1.3

    const val core_ktx: String = "1.3.1" //available: "1.1.0"

    const val espresso_core: String = "3.2.0"

    const val androidx_test_ext_junit: String = "1.1.0" //available: "1.1.1"

    const val material: String = "1.1.0"

    const val retrofit2_kotlin_coroutines_adapter: String = "0.9.2"

    const val com_squareup_okhttp3: String = "4.0.0" //available: "4.3.1"

    const val com_squareup_retrofit2: String = "2.7.1"

    const val arrow_core: String =
        "0.10.4" // No update information. Is this dependency available on jcenter or mavenCentral?

    const val io_mockk: String = "1.9.2" //available: "1.9.3"

    const val jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin: String = "0.2.6"

    const val junit_junit: String = "4.12" //available: "4.13"

    const val kotlin_android_extensions_runtime: String = "1.3.60" //available: "1.3.61"

    const val kotlin_android_extensions: String = "1.3.60" //available: "1.3.61"

    const val kotlin_annotation_processing_gradle: String = "1.3.60" //available: "1.3.61"

    const val kotlin_gradle_plugin: String = "1.3.60" //available: "1.3.61"

    const val kotlin_stdlib_jdk7: String = "1.3.72" //available: "1.3.61"

    const val kotlinx_coroutines_android: String = "1.3.8" //available: "1.3.3-1.3.70-eap-42"

    const val org_koin: String = "2.0.1"

    const val scarlet: String = "0.1.10"

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.6.1"

        const val currentVersion: String = "6.1.1"

        const val nightlyVersion: String = "6.3-20200131230056+0000"

        const val releaseCandidate: String = ""
    }
}
