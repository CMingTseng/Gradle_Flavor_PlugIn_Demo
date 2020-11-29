package com.github.ximik3.gradle.cloud

import com.google.gson.annotations.SerializedName

data class RequirementItem(

        @SerializedName("os")
        val os: String = "",

        @SerializedName("osName")
        val osName: String = "",

        @SerializedName("osVersion")
        val osVersion: String = "",

        @SerializedName("apiLevel")
        val apiLevel: Int = 0
)