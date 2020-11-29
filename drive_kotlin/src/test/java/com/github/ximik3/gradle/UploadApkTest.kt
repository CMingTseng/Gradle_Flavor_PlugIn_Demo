package com.github.ximik3.gradle

import com.github.ximik3.gradle.cloud.csharp.UploadApkApiService
import com.github.ximik3.gradle.cloud.csharp.api.UploadApkApiRepository
import com.github.ximik3.gradle.cloud.csharp.request.PackagesRequest
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.util.*

object UploadApkTest : BaseUploadTask() {

    const val BUILD_TYPE = "dev_debug"
    const val APK_NAME = "ManagerSample-v1.0.15-dev_debug.apk"
    const val packageUniqueId = "org.cks.sample"
    const val packageName = "Manager Sample"
    const val versionStage = 0
    const val compatibilityReleaseId = 0L
    const val UPLOAD_URL = "https://api.yantaiyuecai.com"

    private val ROOT_PATH = File("").absolutePath ?: ""
    private val VERSION_PROPERTIES_PATH = "$ROOT_PATH/app/version.properties"

    private val repository: UploadApkApiRepository by lazy { getApiRepository() }

    @JvmStatic
    fun main(args: Array<String>) {
        runUpload()
    }

    fun runUpload() = runBlocking {
//        print("upload running... \n")
//        val token = getToken()
//        print("upload token:$token \n")
//        val auth = StringBuilder(BaseDomainManager.BEARER_PREFIX)
//            .append(token)
//            .toString()
//        val upload = uploadApk(auth)
//        print("upload uploadResult: $upload} \n")
    }

    private suspend fun getToken(): String {
        val tokenResult = repository.authToken()
        return if (tokenResult.isSuccessful) {
            tokenResult.body()?.accessToken ?: "error"
        } else {
            "error"
        }
    }

    private suspend fun uploadApk(auth: String): String {

        val buildPath = "$ROOT_PATH/app/build/outputs/apk/$BUILD_TYPE/$APK_NAME"
        print("fetchPackagesInfo buildPath: $buildPath \n")

        val apkFile = File(buildPath)
        if (!apkFile.exists()) {
            return "fetchPackagesInfo file is not exist"
        }

        val versionProperties = Properties()
        versionProperties.load(FileInputStream(VERSION_PROPERTIES_PATH))
        val versionMajor = versionProperties["VERSION_CODE"].toString().toInt()
        print("fetchPackagesInfo versionMajor: $versionMajor \n")

        val request = PackagesRequest(
                packageUniqueId = packageUniqueId,
                packageName = packageName,
                versionName = APK_NAME,
                versionMajor = versionMajor,
                versionStage = versionStage,
                compatibilityReleaseId = compatibilityReleaseId
        )

        val uploadApkResult = repository.uploadPackages(
                auth,
                apkFile,
                request)

        print("code: ${uploadApkResult.body()?.code} \n")
        print("message: ${uploadApkResult.body()?.message} \n")
        return if (uploadApkResult.isSuccessful) {
            print("content: ${uploadApkResult.body()?.content} \n")
            uploadApkResult.body()?.message ?: "error"
        } else {
            uploadApkResult.body()?.message ?: "error"
        }
    }

    fun getApiRepository(): UploadApkApiRepository {
        val okHttpClient: OkHttpClient = uploadOkHttpClient(provideHttpLoggingInterceptor())
        val apiService = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .client(okHttpClient)
                .baseUrl(UploadApkTest.UPLOAD_URL)
                .build()
                .create(UploadApkApiService::class.java)
        return UploadApkApiRepository(apiService)
    }
}

