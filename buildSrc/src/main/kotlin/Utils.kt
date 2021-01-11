import org.codehaus.groovy.runtime.ProcessGroovyMethods
import java.io.File

internal fun String.runCommand(workingDir: File, error: Appendable = System.err) {
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