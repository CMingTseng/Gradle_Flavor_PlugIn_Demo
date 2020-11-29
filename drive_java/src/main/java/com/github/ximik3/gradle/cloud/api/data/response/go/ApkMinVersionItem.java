package com.github.ximik3.gradle.cloud.api.data.response.go;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class ApkMinVersionItem {
    @SerializedName("name")
    @NotNull
    private String name;
    @SerializedName("version")
    @NotNull
    private String version;
    @SerializedName("apiLevel")
    private int apiLevel;

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getVersion() {
        return this.version;
    }

    public int getApiLevel() {
        return this.apiLevel;
    }
}

