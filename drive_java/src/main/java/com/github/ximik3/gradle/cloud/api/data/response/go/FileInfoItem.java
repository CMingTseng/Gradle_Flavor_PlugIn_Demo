package com.github.ximik3.gradle.cloud.api.data.response.go;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class FileInfoItem {
    @SerializedName("androidMinVer")
    @NotNull
    private ApkMinVersionItem apkMinVersionItem;
    @SerializedName("androidVersionCode")
    private int androidVersionCode;
    @SerializedName("fileID")
    @NotNull
    private String fileId;
    @SerializedName("androidPackage")
    @NotNull
    private String androidPackage;
    @SerializedName("contentType")
    @NotNull
    private String contentType;
    @SerializedName("downloadedCount")
    private int downloadedCount;
    @SerializedName("fileOriginName")
    @NotNull
    private String fileOriginName;
    @SerializedName("fileExt")
    @NotNull
    private String fileExt;
    @SerializedName("version")
    @NotNull
    private String version;
    @SerializedName("fileSize")
    private int fileSize;
    @SerializedName("imageHeight")
    private int imageHeight;
    @SerializedName("imageWidth")
    private int imageWidth;
    @SerializedName("namespace")
    @NotNull
    private String namespace;

    @NotNull
    public ApkMinVersionItem getApkMinVersionItem() {
        return this.apkMinVersionItem;
    }

    public int getAndroidVersionCode() {
        return this.androidVersionCode;
    }

    @NotNull
    public String getFileId() {
        return this.fileId;
    }

    @NotNull
    public String getAndroidPackage() {
        return this.androidPackage;
    }

    @NotNull
    public String getContentType() {
        return this.contentType;
    }

    public int getDownloadedCount() {
        return this.downloadedCount;
    }

    @NotNull
    public String getFileOriginName() {
        return this.fileOriginName;
    }

    @NotNull
    public String getFileExt() {
        return this.fileExt;
    }

    @NotNull
    public String getVersion() {
        return this.version;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    @NotNull
    public String getNamespace() {
        return this.namespace;
    }
}