package com.github.ximik3.gradle

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class GoUploadApkApiRepository(private val apiService: GoUploadApkApiService) {

    suspend fun authToken(auth: String, request: GoLoginRequest): Response<GoOAutn2TokenItem> {
        return apiService.login(auth = auth, request = request)
    }

    suspend fun uploadPackages(
            auth: String,
            apkFile: File,
            version: Long,
            minVersion: Long
    ): Response<GoUploadItem> {
        val filePart = MultipartBody.Part.createFormData(
                "file", apkFile.name, RequestBody.create(MultipartBody.FORM, apkFile)
        )

        return apiService.upload(
                auth = auth,
                file = filePart,
                version = version,
                minVersion = minVersion
        )
    }

}

//return apiService.authToken(auth = "Basic Mzc4ODI4MDY1MjI5MDMyNjUyODo5NjkzYmYxZmNmZWE0MzI5ODY5OWM1NjI4YTg5YjcyNQ==", request = OAuth2TokenRequest())