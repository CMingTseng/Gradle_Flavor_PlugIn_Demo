package com.github.ximik3.gradle

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface GoUploadApkApiService {

    @POST("/publicDevices/v1/login")
    suspend fun login(
            @Header("Authorization") auth: String,
            @Body request: GoLoginRequest
    ): Response<GoOAutn2TokenItem>

    @Multipart
    @POST("/storage/v1/internal/upload")
    suspend fun upload(
            @Header("Authorization") auth: String,
            @Part file: MultipartBody.Part,
            @Part("version") version: Long,
            @Part("minVersion") minVersion: Long
    ): Response<GoUploadItem>
}
