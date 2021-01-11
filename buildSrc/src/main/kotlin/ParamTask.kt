import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class ParamTask : DefaultTask() {

    init {
        group = "versioning"
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
        "echo =============> userEmail: $userEmail userName: $userName".runCommand(workingDir = project.rootDir)
    }
}