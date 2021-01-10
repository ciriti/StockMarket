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

    @get:Input
    val userName: String by lazy {
        (project.properties["user_name"] as? String) ?: "GitHub Action"
    }

    @get:Input
    val userEmail: String by lazy {
        (project.properties["user_email"] as? String) ?: ""
    }

    @TaskAction
    fun execute() {
        addCommitPush()
    }

    private fun addCommitPush() {
        "echo =============> $userEmail $userName".runCommand(workingDir = project.rootDir)
        "git config user.email $userEmail".runCommand(workingDir = project.rootDir)
        "git config user.name $userName".runCommand(workingDir = project.rootDir)
        "git pull".runCommand(workingDir = project.rootDir)
        val filesList= listOf("")
        filesList.forEach {
            "echo ============= > $it".runCommand(workingDir = project.rootDir)
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