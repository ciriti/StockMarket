import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.text.SimpleDateFormat
import java.util.Date
import java.io.File

open class ReadmeUpdateTask : DefaultTask() {

    init {
        group = "versioning"
    }

    @get:Input
    val readmePath: String by lazy { "${project.rootDir}/README.md" }

    private val mavenRegexDep by lazy { """([\w\.]+):([\w\-]+):(\d)+\.(\d)+\.(\d)+""".toRegex() }

    @get:Input
    val gitAction: String by lazy {
        (project.properties["git_action"] as? String) ?: ""
    }

    @TaskAction
    fun execute() {
        val readme = File(readmePath)
        updateReadme(readme)
        addCommitPush(File(""))
    }

    private fun updateReadme(file : File) {
        // README.md
        val readme = file
        if (!readme.exists()) return
        val readmeContent = readme.readText(charset = Charsets.UTF_8)
        // get versionLib
        val versionLib = project.properties["version_name"] as String
        val updateReadme = readmeContent.replace(mavenRegexDep, "io.github.ciriti:okhttp-socket-ext:$versionLib")
        // README.md content updated
        readme.writeText(text = updateReadme, charset = Charsets.UTF_8)
    }

    private fun addCommitPush(file : File) {
        if (gitAction == "push") {
            "git fetch".runCommand(workingDir = project.rootDir)
            "git add ${file.path}".runCommand(workingDir = project.rootDir)
            "git commit -m \"CHANGELOG.md updated\"".runCommand(workingDir = project.rootDir)
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