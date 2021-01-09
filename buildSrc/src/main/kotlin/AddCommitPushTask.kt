import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

open class AddCommitPushTask : DefaultTask() {

    init {
        group = "versioning"
    }

    @get:Input
    val gitAction: String by lazy {
        (project.properties["git_action"] as? String) ?: ""
    }

    @TaskAction
    fun execute() {
        addCommitPush()
    }

    private fun addCommitPush() {
        if (gitAction == "push") {
            // git fetch
            "git fetch".runCommand(workingDir = project.rootDir)
            "git add ${project.rootDir}/test.txt".runCommand(workingDir = project.rootDir)
            "git commit -m \"test.txt done\"".runCommand(workingDir = project.rootDir)
            "git push".runCommand(workingDir = project.rootDir)
        }
    }

    fun String.runCommand(
        workingDir: File = File(".")
    ): String {
        val process: Process = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.waitForProcessOutput(System.out, System.err)
        return ""
    }

    fun Process.waitForProcessOutput(
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