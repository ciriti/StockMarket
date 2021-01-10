import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

open class AddCommitPushTask : DefaultTask() {

    init {
        group = "versioning"
    }

    @get:Input
    val filesArg: String by lazy {
        (project.properties["files"] as? String) ?: ""
    }

    val filesList: List<String> by lazy { filesArg.split(":") }

    @TaskAction
    fun execute() {
        addCommitPush()
    }

    private fun addCommitPush() {
        "echo rootDir[${project.rootDir}]".runCommand(workingDir = project.rootDir)
        "git config user.email ciriti@gmail.com".runCommand(workingDir = project.rootDir)
        "git config user.name GitHub Action".runCommand(workingDir = project.rootDir)
        "git pull".runCommand(workingDir = project.rootDir)
        filesList.forEach {
            "git add $it".runCommand(workingDir = project.rootDir)
        }
        "git commit -m \"committed files $filesList\"".runCommand(workingDir = project.rootDir)
        "git push".runCommand(workingDir = project.rootDir)
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