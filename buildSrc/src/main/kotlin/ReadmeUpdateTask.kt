import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ReadmeUpdateTask : DefaultTask() {

    init {
        group = "versioning"
    }

    @get:Input
    val readmePath: String by lazy { "${project.rootDir}/README.md" }

    private val mavenRegexDep by lazy { """(\d)+\.(\d)+\.(\d)+""".toRegex() }
    private val versionLib by lazy { project.properties["version_name"] as String }

    @TaskAction
    fun execute() {
        val readme = File(readmePath)
        updateReadme(readme)
    }

    private fun updateReadme(file: File) {
        // README.md
        if (!file.exists()) return
        val readmeContent = file.readText(charset = Charsets.UTF_8)
        val updateReadme = readmeContent.replace(mavenRegexDep, versionLib)
        // README.md content updated
        file.writeText(text = updateReadme, charset = Charsets.UTF_8)
    }

    private fun String.updateMavenDependency(version: String): String {
        val list = this.split(":").toMutableList()
        if (list.size < 3) return this
        list[2] = version
        return list.joinToString(separator = ":")
    }
}