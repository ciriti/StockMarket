package io.github.ciriti.gitutils

import io.github.ciriti.gitutils.Constants.GROUP
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.lang.StringBuilder
import javax.inject.Inject

open class GitUtilsTask @Inject constructor(
    private val ext: GitUtilsExt
) : DefaultTask() {

    init {
        group = GROUP
    }

    @get:Input
    val userName: String by lazy { System.getenv("GIT_USERNAME")?:"ciriti" }

    @get:Input
    val userEmail: String by lazy { System.getenv("GIT_EMAIL")?:"ciriti@gmail.com" }

    @TaskAction
    fun execute() {
        val content = ext
            .fileList
            .map { File(it) }
            .toList()
            .run(::addCommitAndPush)
        println(content)
    }


    private fun addCommitAndPush(files: List<File>): String {
        val error = ErrorAppend()
        "echo =============> userEmail: $userEmail userName: $userName".runCommand(error = error)
        "git config user.email $userEmail".runCommand(error = error)
        "git config user.name $userName".runCommand(error = error)
        "git pull --ff-only".runCommand(error = error)
        files
            .forEach {
                checkFile(it)
                "echo ============= > $it".runCommand(error = error)
                "git add $it".runCommand(error = error)

            }
        "git commit -m \"committed files $files\"".runCommand(error = error)
        "git push".runCommand(error = error)

        "echo ======= ERROR ========".runCommand(error = error)
        "echo ${error.sb}".runCommand(error = error)
        "echo ======================".runCommand(error = error)
        return "Success!!!"
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

    class ErrorAppend : Appendable {
        val sb by lazy { StringBuilder() }
        override fun append(csq: CharSequence?): java.lang.Appendable {
            sb.append("$csq\n")
            return System.err
        }

        override fun append(csq: CharSequence?, start: Int, end: Int): java.lang.Appendable {
            sb.append("$csq\n")
            return System.err
        }

        override fun append(c: Char): java.lang.Appendable {
            sb.append(c)
            return System.err
        }
    }

    private fun checkFile(file: File) {
        if (!file.exists()) {
            throw GradleException(
                """
                    the file [${file.path}] does not exist!!!
                """.trimIndent()
            )
        }
    }
}
