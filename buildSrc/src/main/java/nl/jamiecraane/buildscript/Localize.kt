package nl.jamiecraane.buildscript

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Gradle task which calls the localize function for all *.csv files and places the outputDirName.
 */
open class Localize : DefaultTask() {
    lateinit var outputFolder: File
    lateinit var repoLocation: File

    @TaskAction
    fun localize() {
        if (outputFolder.exists()) {
            outputFolder.deleteRecursively()
        }

        outputFolder.mkdirs()

        repoLocation
            .list()
            .filter { it.endsWith(".csv") }
            .forEach {
                project.exec({
                    workingDir = repoLocation
                    val outputFileName = "${it.substring(0, it.lastIndexOf("."))}.xml"
                    val commands = listOf("./localizable", "-c", it, "-a", "${outputFolder.path}/strings_$outputFileName")
                    println("commands $commands")
                    commandLine = commands
                })
            }
    }
}