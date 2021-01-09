import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.text.SimpleDateFormat
import java.util.Date
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

open class ChangeLogUpdateTask : DefaultTask() {

    init { group = "versioning" }

    @get:Input
    val releaseNotePath: String by lazy {
        (project.properties["releaseNotePath"] as? String) ?: "${project.rootDir}/release_note.txt"
    }
    @get:Input
    val changeLogPath: String by lazy {
        (project.properties["changeLogPath"] as? String) ?: "${project.rootDir}/CHANGELOG.md"
    }

    @get:Input
    val gitAction: String by lazy {
        (project.properties["git_action"] as? String) ?: ""
    }

    @TaskAction
    fun execute() {
        // CHANGELOG.md
        val changeLog = File(changeLogPath)
        if (!changeLog.exists()) changeLog.createNewFile()
        val changeLogContent = changeLog.readText(charset = Charsets.UTF_8)
        // release_note.txt
        val releaseNote = File(releaseNotePath)
        val releaseNoteContent = releaseNote.readText(charset = Charsets.UTF_8)
        // January, 09, 2021
        val date: String = SimpleDateFormat("MMMM, DD, YYYY").format(Date())
        // X.Y.Z
        val versionLib = project.properties["version_name"] as String
        // CHANGELOG.md content updated
        val updatedChangeLog = "## $versionLib ($date) \n$releaseNoteContent \n\n$changeLogContent".trimMargin()
        changeLog.writeText(text = updatedChangeLog, charset = Charsets.UTF_8)
    }

    private fun addCommitPush(){
        if(gitAction == "push"){
            // git fetch
            "git fetch".runCommand(workingDir = project.rootDir)
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