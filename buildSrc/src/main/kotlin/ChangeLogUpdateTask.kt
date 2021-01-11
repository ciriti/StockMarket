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

    private val versionLib by lazy { project.properties["version_name"] as String }

    @TaskAction
    fun execute() {
        updateChangelog()
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
        // CHANGELOG.md content updated
        val updatedChangeLog =
            "## $versionLib ($date) \n${releaseNoteContent.trim()} \n\n$changeLogContent".trimMargin()
        changeLog.writeText(text = updatedChangeLog, charset = Charsets.UTF_8)
    }
}