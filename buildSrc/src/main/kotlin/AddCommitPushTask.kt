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
//            val fetchChanges : Process = ['git', 'fetch'].execute(null, project.rootDir)
//            fetchChanges.waitForProcessOutput(System.out, System.err)
//            // git add changeLogPath
//            Process addChanges = ['git', 'add', changeLogPath].execute(null, project.rootDir)
//            addChanges.waitForProcessOutput(System.out, System.err)
//            // git commit -m "$commitMessage"
//            Process createCommit = ['git', 'commit', '-m', commitMessage].execute(null, project.rootDir)
//            createCommit.waitForProcessOutput(System.out, System.err)
//            // git push
//            Process push = ['git', 'push'].execute(null, project.rootDir)
//            push.waitForProcessOutput(System.out, System.err)
        }
    }

    fun String.runCommand(
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
    ): String = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .apply { waitFor(timeoutAmount, timeoutUnit) }
        .run {
            val error = errorStream.bufferedReader().readText().trim()
            if (error.isNotEmpty()) {
                throw IOException(error)
            }
            inputStream.bufferedReader().readText().trim()
        }
}