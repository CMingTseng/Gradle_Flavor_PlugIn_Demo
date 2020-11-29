package com.github.ximik3.gradle.utils

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object ResourceLoader {

    fun getResourceFile(path: String): File? = this.javaClass.classLoader
            .getResource(path)
            ?.toURI()
            ?.let { uri -> File(uri) }

    fun readFileAsString(path: String): String {
        val file = getResourceFile(path) ?: throw RuntimeException("no such file")
        println("Show  file state   :  ${file.exists()}")
        return BufferedInputStream(FileInputStream(file)).use { inputStream ->
            String(inputStream.readBytes())
        }
    }

    fun openStream(path: String): InputStream = this.javaClass.classLoader
            .getResourceAsStream(path) ?: throw RuntimeException("no such file")
}