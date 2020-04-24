package nl.jamiecraane.buildscript

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

/**
 * Moves the generated localized xml files to the correct source folders in the project.
 */
open class MoveLocalizedFiles : DefaultTask() {
    @OutputDirectory
    lateinit var output: File
    @InputFiles
    lateinit var source: File

    @TaskAction
    fun moveFiles() {
        output
            .list()
            .forEach { moveFile(it) }
    }

    fun moveFile(file: String) {
        // all files with name other than generic are flavors fow now.
        val nameWithoutLang = file.substring(file.indexOf(".") + 1, file.length)
        val lang = file.substring(0, file.indexOf("."))
        val flavor = nameWithoutLang.substring(nameWithoutLang.indexOf("_") + 1, nameWithoutLang.lastIndexOf("."))
        println("FLAVOR = $flavor")
        val flavorFolder = getFlavorFolder(flavor)
        println("FLAVORFOLDER = $flavorFolder")
        val fileToCopy = File(output, file)
        val destination = File(source, "$flavorFolder/res/${getValuesFolder(lang)}/$nameWithoutLang")
        println("DESTINATION = $destination")
        fileToCopy.copyTo(destination, true)
    }

    fun getFlavorFolder(name: String) = if (name == "generic") {
        "main"
    } else {
        "flavor_$name"
    }

    fun getValuesFolder(lang: String) = if (lang == DEFAULT_LANGUAGE) {
        VALUES_FOLDER
    } else {
        "$VALUES_FOLDER-$lang"
    }

    companion object {
        private const val DEFAULT_LANGUAGE = "nl"
        private const val VALUES_FOLDER = "values"
    }
}
