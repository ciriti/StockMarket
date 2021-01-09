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

    private val mavenRegexDep by lazy { """([\w\.]+):([\w\-]+):(\d)+\.(\d)+\.(\d)+""".toRegex() }
    private val versionLib by lazy { project.properties["version_name"] as String }

    @get:Input
    val gitAction: String by lazy {
        (project.properties["git_action"] as? String) ?: ""
    }

    @TaskAction
    fun execute() {
        val readme = File(readmePath)
        updateReadme(readme)
        addCommitPush(readme)
    }

    private fun updateReadme(file: File) {
        // README.md
        if (!file.exists()) return
        val readmeContent = file.readText(charset = Charsets.UTF_8)
        val updateReadme = readmeContent.replace(mavenRegexDep, "io.github.ciriti:okhttp-socket-ext:$versionLib")
        // README.md content updated
        file.writeText(text = updateReadme, charset = Charsets.UTF_8)
    }

    private fun addCommitPush(file: File) {
        if (gitAction == "push") {
            "git fetch".runCommand(workingDir = project.rootDir)
            "git add ${file.path}".runCommand(workingDir = project.rootDir)
            "git commit -m \"README.md updated with version $versionLib\"".runCommand(workingDir = project.rootDir)
            "git push".runCommand(workingDir = project.rootDir)
        }
    }

    private fun String.runCommand(workingDir: File = File(".")) {
        val process: Process = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.waitForProcessOutput(System.out, System.err)
    }

    private fun Process.waitForProcessOutput(
        output: Appendable,
        error: Appendable
    ) {
        val tout = ProcessGroovyMethods.consumeProcessOutputStream(this, output)
        val terr = ProcessGroovyMethods.consumeProcessErrorStream(this, error)
        tout.join()
        terr.join()
        this.waitFor()
        ProcessGroovyMethods.closeStreams(this)
    }
}