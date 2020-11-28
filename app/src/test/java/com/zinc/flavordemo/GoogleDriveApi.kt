package com.example.myapplication

import com.google.gson.GsonBuilder
import com.zinc.flavordemo.Credential
import com.zinc.flavordemo.GoogleDriveUploadItem
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.http.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

interface GoogleDriveApi {
    companion object {
        //The number of bytes uploaded is required to be equal or greater than 262144, except for the final request (it's recommended to be the exact multiple of 262144)
        const val RESUMABLE_UPLOAD_PART_MAX_LENGTH = 256 * 1024 // bytes
    }

    @Headers(
            "Content-Type: application/json; charset=UTF-8",
            "X-Upload-Content-Type: application/octet-stream"
    )
    @POST("upload/drive/v3/files?uploadType=resumable")
    fun resumableUpload(
            @Header("Authorization")
            auth: String,
            @Header("X-Upload-Content-Length")
            contentLength: Long,
            @Body
            fileMetadata: String
    ): Call<String>

    @GET("drive/v3/files")
    fun files(
            @Header("Authorization")
            auth: String
    ): Call<String>

}

fun GoogleDriveApi.resumableUpload(
        credential: Credential,
        file: File,
        name: String,
        parentId: String
): GoogleDriveUploadItem {
    require(file.exists()) { "no such file" }
    val gson = GsonBuilder().setPrettyPrinting().create()
    val infos = GoogleDriveUploadItem()
    infos.addFolderIds(parentId)
    infos.mFileName = name
    infos.file = file

//    val response = resumableUpload(
//        credential.authentication,
//        file.length(),
//        "{\"name\": \"$name\", \"parents\": [\"$parentId\"]}"
//    ).execute()
    val response = resumableUpload(
            credential.authentication,
            file.length(), gson.toJson(infos)
    ).execute()
    if (response.code() != 200) throw HttpException(response)
    //    2 = "X-GUploader-UploadID"
    //    3 = "ABg5-UxNZ2wlMaXBaFL9-g9ZXuPV63bgA1OalNiJ4e-iiSTSHHz4bCXxQRd4S0yDkVBz7nD7p1U1QikqnoqMFk81VdfLWIo9aw"
    //    4 = "Location"
    //    5 = "https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable&upload_id=ABg5-UxNZ2wlMaXBaFL9-g9ZXuPV63bgA1OalNiJ4e-iiSTSHHz4bCXxQRd4S0yDkVBz7nD7p1U1QikqnoqMFk81VdfLWIo9aw"
    val map = response.headers().toMultimap()
    val uploadId = map["X-GUploader-UploadID"]?.get(0) ?: throw RuntimeException("illegal response")
    val uploadUrl = map["Location"]?.get(0) ?: throw RuntimeException("illegal response")
    infos.mFileUploadId = uploadId
    infos.mFileUploadUrl = uploadUrl
    return infos
}

// https://developers.google.com/drive/api/v3/manage-uploads#resume-upload
fun GoogleDriveApi.uploadFile(
        okHttpClient: OkHttpClient,
        credential: Credential,
        file: File,
        name: String,
        parentId: String
) {
    fun getInputStream() = BufferedInputStream(FileInputStream(file))
    val item = resumableUpload(credential, file, name, parentId)
    val totalSize = file.length()
    var lastByteCount = 0L

    var inputStream = getInputStream()
    val byteArray = ByteArray(GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH)
    fun readData(): Int {
        var length = 0
        while (length != GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH) {
            println("Show  byteArray ? " + byteArray.size)
            println("Show  off length ? $length")
            println("Show  GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH ? ${GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH}")
            println("Show  len ? ${(GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH - length)})")
            val readLength = inputStream.read(
                    byteArray,
                    length,
                    GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH - length
            )
            if (readLength == -1) break
            length += readLength
        }
        return length
    }

    whileLoop@ while (lastByteCount != totalSize) {
        val length = readData()
        println("Show  whileLoop length ? $length")
        // This is weird
        if (length == 0) break

        val requestBody = RequestBody.create(null, byteArray, 0, length)
        val put = Request.Builder()
                .put(requestBody)
//            .url(item.uploadUrl)
                .url(item.mFileUploadUrl)
                .addHeader("Authorization", credential.authentication)
                .addHeader("Content-Length", length.toString())
                .addHeader(
                        "Content-Range",
                        "bytes $lastByteCount-${lastByteCount + length - 1}/$totalSize"
                )
                .build()
        val putResponse = okHttpClient.newCall(put).execute()
        when (putResponse.code()) {
            200 -> {
                break@whileLoop
            }
            // Resume Incomplete
            308 -> {
                val nextPosition =
                        putResponse.header("Range")!!.split("=")[1].split("-")[1].toLong() + 1
                // Something wrong with previous request, we need to reset the inputStream
                lastByteCount = if (lastByteCount + length != nextPosition) {
                    inputStream.close()
                    inputStream = getInputStream()
                    inputStream.skip(nextPosition)
                    nextPosition
                } else {
                    lastByteCount + length.toLong()
                }
            }
            else -> {
                println("Response Code: ${putResponse.code()}")
            }
        }
    }
}
