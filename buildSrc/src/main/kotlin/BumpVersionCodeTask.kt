import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.*

open class BumpVersionCodeTask : DefaultTask() {

    init {
        group = "versioning"
    }

    @TaskAction
    fun execute() {
        val file = File("${project.projectDir}/gradle.properties")
        if(!file.exists()) throw GradleException("""
            gradle.properties doesn't exist!!!
            Create a gradle.properties file into the following dir [${project.projectDir}]
        """.trimIndent())
        updateVersionCode(file)
    }

    private fun updateVersionCode(file: File) {

        val versionProps = Properties()
        versionProps.load(file.inputStream())

        if(!versionProps.containsKey("VERSION_CODE"))throw GradleException("""
            Property VERSION_CODE doesn't exist!!!
            Insert a property VERSION_CODE file into the gradle.properties file. Ex:
            VERSION_CODE=10
        """.trimIndent())

        versionProps.getProperty("VERSION_CODE").toIntOrNull() ?: throw GradleException("""
            VERSION_CODE doesn't contain a number!!!
            Replace the VERSION_CODE value with a number
        """.trimIndent())

        val bumpedVersionCode = versionProps.getProperty("VERSION_CODE").toLong().inc()
        versionProps.setProperty("VERSION_CODE", "$bumpedVersionCode")
        versionProps.store(file.writer(), null)
    }
}