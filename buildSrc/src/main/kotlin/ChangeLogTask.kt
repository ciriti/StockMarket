import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.text.SimpleDateFormat
import java.util.Date
import java.io.File

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
//            val process = "git describe --exact-match".execute(null, rootDir)
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
}