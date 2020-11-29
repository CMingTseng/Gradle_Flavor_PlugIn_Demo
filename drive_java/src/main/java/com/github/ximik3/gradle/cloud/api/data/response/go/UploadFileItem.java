package com.github.ximik3.gradle.cloud.api.data.response.go;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class UploadFileItem {
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(@NotNull String url) {
        this.url = url;
    }
}
