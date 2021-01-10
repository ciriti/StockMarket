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

    @get:Input
    val gitAction: String by lazy {
        (project.properties["git_action"] as? String) ?: ""
    }

    @TaskAction
    fun execute() {
        val readme = File(readmePath)
        updateReadme(readme)
//        addCommitPush(readme)
    }

    private fun updateReadme(file: File) {
        // README.md
        if (!file.exists()) return
        val readmeContent = file.readText(charset = Charsets.UTF_8)
        val updateReadme = readmeContent.replace(mavenRegexDep, versionLib)
        // README.md content updated
        file.writeText(text = updateReadme, charset = Charsets.UTF_8)
    }

    private fun addCommitPush(file: File) {
        if (gitAction == "push") {
            "git config user.email ciriti@gmail.com".runCommand(workingDir = project.rootDir)
            "git config user.name GitHub Action".runCommand(workingDir = project.rootDir)
            "git fetch".runCommand(workingDir = project.rootDir)
            "git add ${file.path}".runCommand(workingDir = project.rootDir)
            "git commit -m \"Version $versionLib - README.md updated\"".runCommand(workingDir = project.rootDir)
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

    private fun String.updateMavenDependency(version : String) : String{
        val list = this.split(":").toMutableList()
        if(list.size < 3) return this
        list[2] = version
        return list.joinToString(separator = ":")
    }
}