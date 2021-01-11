import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.lang.StringBuilder

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
        val error = ErrorAppend()
        "echo =============> userEmail: $userEmail userName: $userName".runCommand(error = error)
        "git config user.email $userEmail".runCommand(error = error)
        "git config user.name $userName".runCommand(error = error)
        "git pull".runCommand(error = error)
        val filesList= filesArg.split(":")
        filesList.forEach {
            "echo ============= > $it".runCommand(error = error)
            "git add $it".runCommand(error = error)
        }
        "git commit -m \"committed files $filesList\"".runCommand(error = error)
        "git push".runCommand(error = error)

        "echo ======= ERROR ========".runCommand(error = error)
        "echo ${error.sb}".runCommand(error = error)
        "echo ======================".runCommand(error = error)
    }

    private fun String.runCommand(workingDir: File = project.rootDir, error: Appendable) {
        val process: Process = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.waitForProcessOutput(System.out, error)

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

    class ErrorAppend : Appendable{
        val sb by lazy { StringBuilder() }
        override fun append(csq: CharSequence?): java.lang.Appendable {
            sb.append(csq)
            return System.err
        }

        override fun append(csq: CharSequence?, start: Int, end: Int): java.lang.Appendable {
            sb.append(csq)
            return System.err
        }

        override fun append(c: Char): java.lang.Appendable {
            sb.append(c)
            return System.err
        }
    }
}