package com.github.ximik3.gradle.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

fun isJSONValid(jsonInString: String?): Boolean {
    return try {
        Gson().fromJson(jsonInString, Any::class.java)
        true
    } catch (ex: JsonSyntaxException) {
        false
    }
}

@Throws(IOException::class)
fun inputStreamToString(stream: InputStream?): String? {
    val bufferSize = 1024
    val buffer = CharArray(bufferSize)
    val out = StringBuilder()
    val `in`: Reader = InputStreamReader(stream, StandardCharsets.UTF_8)
    var charsRead: Int
    while (`in`.read(buffer, 0, buffer.size).also { charsRead = it } > 0) {
        out.append(buffer, 0, charsRead)
    }
    return out.toString()
}

//FIXME !!
//fun readLineByLineJava8(filePath: String?): String? {
//    val contentBuilder = java.lang.StringBuilder()
//    try {
//        Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).use { stream ->
//            stream.forEach { s: String? ->
//                contentBuilder.append(
//                    s
//                ).append("\n")
//            }
//        }
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//    return contentBuilder.toString()
//}

//    private static final String readLineByLineJava8(String filePath) {
//        StringBuilder contentBuilder = new StringBuilder();
//        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
//            stream.forEach(s -> contentBuilder.append(s).append("\n"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return contentBuilder.toString();
//    }

fun readAllBytesJava7(filePath: String?): String? {
    var content = ""
    try {
        content = String(Files.readAllBytes(Paths.get(filePath)))
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return content
}

fun readAllBytesJava6usingBufferedReader(filePath: String?): String? {
    val contentBuilder = java.lang.StringBuilder()
    try {
        BufferedReader(FileReader(filePath)).use { br ->
            var sCurrentLine: String?
            while (br.readLine().also { sCurrentLine = it } != null) {
                contentBuilder.append(sCurrentLine).append("\n")
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return contentBuilder.toString()
}


