import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.plugin.management.internal.autoapply.AutoAppliedGradleEnterprisePlugin.GROUP
import javax.inject.Inject

open class GitUtilsTask @Inject constructor() : DefaultTask() {

    init {
        group = GROUP
    }

    companion object{
        const val KEY_USERNAME = "GIT_USERNAME"
        const val KEY_EMAIL = "GIT_EMAIL"
    }

    @get:Input
    var userName: String = ""

    @get:Input
    var userEmail: String = ""

    @TaskAction
    fun execute() {
        userName = System.getenv(KEY_USERNAME) ?: fail(KEY_USERNAME)
        userEmail = System.getenv(KEY_EMAIL) ?: fail(KEY_EMAIL)
        println("userName: $userName")
        println("userEmail: $userEmail")
    }

    private fun fail(key: String): Nothing{
        throw GradleException("""
            The environment variable [$key] is null!!!
            Please set [GIT_USERNAME] and [GIT_EMAIL] variables in you system:
            ...
            GIT_USERNAME=<your github username>
            GIT_EMAIL=<your github email>
            ...
        """.trimIndent())
    }

}
