import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

open class ChangeLogUpdateTask : DefaultTask() {

    init {
        group = "versioning"
    }

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

    private val versionLib by lazy { project.properties["version_name"] as String }

    @TaskAction
    fun execute() {
        val dir = File("/Users/runner/work/_temp/_runner_file_commands")
        dir
            .listFiles()
            ?.filter { it.isFile }
            ?.forEach {
                "echo $it".runCommand(workingDir = project.rootDir)
                "echo \"VERSION_NAME=$versionLib\" >> $it".runCommand(workingDir = project.rootDir)
            } ?: kotlin.run {
            "echo NO listFiles =============== ".runCommand(workingDir = project.rootDir)
        }
        "echo \"VERSION_NAME=$versionLib\" >> \$GITHUB_ENV".runCommand(workingDir = project.rootDir)
        updateChangelog()
//        addCommitPush()
    }

    private fun updateChangelog() {
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
        val updatedChangeLog =
            "## $versionLib ($date) \n${releaseNoteContent.trim()} \n\n$changeLogContent".trimMargin()
        changeLog.writeText(text = updatedChangeLog, charset = Charsets.UTF_8)
    }

    private fun addCommitPush() {
        if (gitAction == "push") {
            "git config user.email ciriti@gmail.com".runCommand(workingDir = project.rootDir)
            "git config user.name GitHub Action".runCommand(workingDir = project.rootDir)
            "git fetch".runCommand(workingDir = project.rootDir)
            "git add $changeLogPath".runCommand(workingDir = project.rootDir)
            "git commit -m \"Version $versionLib - CHANGELOG.md updated\"".runCommand(workingDir = project.rootDir)
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