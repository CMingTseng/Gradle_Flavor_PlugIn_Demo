package com.github.ximik3.gradle

import java.io.File
import java.io.IOException

class DriveUploader internal constructor(accessToken: com.github.ximik3.gradle.cloud.Credential) {
    private val accessToken: com.github.ximik3.gradle.cloud.Credential

    @Throws(IOException::class, SecurityException::class)
    fun upload(file: File, mimeType: String?, driveFolderId: String?) {
//        println('Uploading ' + file.name + "...")
        val uploadId = retrieveUploadId(file.name, mimeType, driveFolderId)
        //        uploadFile(file, mimeType, uploadId)
    }

    fun retrieveUploadId(filename: String?, mimeType: String?, folderId: String?): String {
//        def url = new URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable")
//        def drive = url.openConnection() as HttpURLConnection
//        drive.setRequestMethod("POST")
//        drive.setRequestProperty("Host", "www.googleapis.com")
//        drive.setRequestProperty("Authorization", accessToken.tokenType + " " + accessToken.accessToken)
//        drive.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
//        drive.setRequestProperty("X-Upload-Content-Type", mimeType)
//        def body = '\n' +
//                '{\n' +
//                '  "name": "' + filename + '",\n' +
//                '  "parents": [ "' + folderId + '" ]\n' +
//                '}\n\n'
//        drive.setFixedLengthStreamingMode body.bytes.length     // Content Length
//        drive.setDoOutput true
//        drive.setDoInput true
//        drive.outputStream.write body.bytes
//
//        // response header example:
//        //   -> Location: [https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable&upload_id=AEnB...]
//        // get "Location" header
//        String loc = drive.getHeaderFields().get("Location")
//
//        // trim '[' and ']' brackets and get URL query
//        def query = new URL(loc[1..-2]).query
//
//        // find upload_id value
//        String uploadId = ''
//        query.split('&').each {
//            if (it.split('=')[0] == 'upload_id')
//                uploadId = it.split('=')[1]
//        }

//        return uploadId;
        return ""
    }

    companion object {
        fun uploadFile(file: File?, mimeType: String?, uploadId: String?) {
//        def url = new URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable&upload_id=" + uploadId)
//        def drive = url.openConnection() as HttpURLConnection
//        drive.setRequestMethod 'POST'
//        drive.setRequestProperty("Content-Type", mimeType)
//        drive.setFixedLengthStreamingMode(file.bytes.length)     // Content Length
//        drive.setDoOutput true
//        drive.setDoInput true
//        drive.outputStream.write file.bytes
        }
    }

    init {
        this.accessToken = accessToken
    }
}