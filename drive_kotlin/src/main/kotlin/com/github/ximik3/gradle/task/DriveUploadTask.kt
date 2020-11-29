package com.github.ximik3.gradle.task

import com.android.build.gradle.api.ApplicationVariant
import com.github.ximik3.gradle.DrivePluginExtension
import org.gradle.api.DefaultTask

class DriveUploadTask : DefaultTask() {
    val APK_MIME_TYPE = "application/vnd.android.package-archive"
    var extension: DrivePluginExtension? = null
    var variant: ApplicationVariant? = null
    var uploader: com.github.ximik3.gradle.DriveUploader? = null
    fun upload() {
//        if (uploader == null) {
//            AccessToken accessToken = AuthorizationHelper.authorize(extension);
//            println 'Token from googleapis.com retrieved'
//
//            uploader = new DriveUploader(accessToken);
//        }
//        variant.outputs
//                .findAll() { variantOutput -> variantOutput instanceof ApkVariantOutput
//        }
//                .each { variantOutput -> uploader.upload(variantOutput.outputFile, APK_MIME_TYPE, extension.folderId)
//        }
    }
}