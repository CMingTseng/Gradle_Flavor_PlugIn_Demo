package com.github.ximik3.gradle.task

import com.android.build.gradle.api.ApplicationVariant
import com.github.ximik3.gradle.cloud.go.ApiService
import com.github.ximik3.gradle.cloud.go.request.LoginRequest
import com.github.ximik3.gradle.cloud.go.response.FileInfoItem
import com.github.ximik3.gradle.cloud.go.response.LoginItem
import com.github.ximik3.gradle.cloud.go.response.UploadFileItem
import com.github.ximik3.gradle.enums.ApkUpgradeType
import com.github.ximik3.gradle.enums.ApkUpgradeType.*
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.tasks.TaskAction
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

open class GoUploadTask : DefaultTask() {
    var flavor: String = ""
    var buildType: String = ""
    var upgradeType: ApkUpgradeType? = null
    var version: String = ""
    var variant: ApplicationVariant? = null

    private lateinit var apiService: ApiService

    @TaskAction
    fun upload() = runBlocking {
        println("** [uploadApk_${flavor}_$buildType] start **\n* upgradeType:$upgradeType\n* version:$version")

        // get gradle ext properties
        val properties = getExtraPropertiesExtension().properties

        // 1. get original apk path
        // 2. calculate apk name & path for upload
        var originalApkPath = ""
        variant?.outputs?.all { output ->
            originalApkPath = output.outputFile.absolutePath
        }
        val apkPath: String
        apkPath = if (originalApkPath.contains("GAM-")) {
            originalApkPath.replace("-v\\S*\\.apk".toRegex(), "") + ".apk"
        } else {
            originalApkPath.replace("_v\\S*\\.apk".toRegex(), "") + ".apk"
        }
        val apkName = apkPath.substring(apkPath.lastIndexOf("/") + 1)
        println("* apkName: $apkName")
        println("* apkPath: $apkPath")

        // 1. get uploadHosts from gradle ext
        // 2. calculate domain
        val uploadHost =
                if (originalApkPath.contains("GAM-"))
                    variant?.productFlavors?.get(0)?.buildConfigFields?.get("GAM_API_HOST")?.value.toString()
                else
                    (properties["uploadHosts"] as HashMap<*, *>)[flavor + buildType] as String
        val domain =
                uploadHost.substring(uploadHost.indexOf("://") + 3).replace("/".toRegex(), "")
                        .replace("\"", "")
        println("* domain: $domain")

        // copy apk for upload
        try {
            FileUtils.copyFile(File(originalApkPath), File(apkPath))
        } catch (e: IOException) {
            e.printStackTrace()
            return@runBlocking
        }

        // setup api service
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

        apiService = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .client(okHttpClient)
                .baseUrl("https://$domain")
                .build()
                .create(ApiService::class.java)

        // login to get access token
        val loginItem = login() ?: return@runBlocking

        // 1. get current file info
        // 2. calculate minVersion by get file info result and upgradeType
        val fileInfoBeforeUpload = getFileInfo(apkName)
        val minVersion =
                if (fileInfoBeforeUpload == null) { //not upload yet
                    version
                } else {
                    when (upgradeType) {
                        NOT_FORCE_UPGRADE -> fileInfoBeforeUpload.minVersion
                        FORCE_UPGRADE_TO_LAST_VERSION -> fileInfoBeforeUpload.version
                        FORCE_UPGRADE_TO_NEW_VERSION -> version
                        else -> ""
                    }
                }

        // upload file
        val file = File(apkPath)
        uploadFile("Bearer ${loginItem.accessToken}", file, minVersion) ?: return@runBlocking

        // delete copied file
        File(apkPath).deleteOnExit()

        // check is upload success
        val fileInfoAfterUpload = getFileInfo(apkName)
        if (fileInfoAfterUpload == null) {
            println("** [uploadApk_${flavor}_$buildType] fail **\n** check if your upload file existed, domain or plugin setting are correct **")
        } else {
            println("** [uploadApk_${flavor}_$buildType] success **")
        }
    }

    private fun getExtraPropertiesExtension(): ExtraPropertiesExtension {
        return project.extensions.getByType(ExtraPropertiesExtension::class.java)
    }

    private suspend fun getFileInfo(name: String): FileInfoItem? {
        println("* [getFileInfo] api *****************************************************")
        val result = apiService.getFileInfo(name)
        return if (result.isSuccessful) {
            val info = result.body()?.data
            println("  SUCCESS: version=${info?.version}, minVersion=${info?.minVersion}")
            println("  ${Gson().toJson(info)}")
            info
        } else {
            println("  FAIL:\n  ${HttpException(result).response()?.errorBody()?.string()}")
            null
        }
    }

    private suspend fun login(): LoginItem? {
        println("* [login] api ***********************************************************")
        val request = LoginRequest(
                "sakjfndsj12345",
                "OPI0001",
                "app001",
                "12345678",
                4,
                "com.dabenxiang.plugin"
        )
        val result = apiService.login(request)
        return if (result.isSuccessful) {
            println("  SUCCESS:\n  ${Gson().toJson(result.body()?.data)}")
            result.body()?.data
        } else {
            println("  FAIL:\n  ${HttpException(result).response()?.errorBody()?.string()}")
            null
        }
    }

    private suspend fun uploadFile(auth: String, file: File, minVersion: String): UploadFileItem? {
        println("* [upload] api ***********************************************************")
        val requestFile = MultipartBody.Part.createFormData(
                "file", file.name, RequestBody.create(MultipartBody.FORM, file)
        )
        val result = apiService.uploadFile(
                auth = auth,
                file = requestFile,
                version = version.toLong(),
                minVersion = minVersion.toLong()
        )
        return if (result.isSuccessful) {
            println("  SUCCESS:\n  ${Gson().toJson(result.body()?.data)}")
            result.body()?.data
        } else {
            println("  FAIL:\n  ${HttpException(result).response()?.errorBody()?.string()}")
            null
        }
    }

}
