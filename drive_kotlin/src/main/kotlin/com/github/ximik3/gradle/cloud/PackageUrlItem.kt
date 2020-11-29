package com.github.ximik3.gradle.cloud

import com.google.gson.annotations.SerializedName

data class PackageUrlItem(

        @SerializedName("main")
        val main: String = "",

        @SerializedName("mirror")
        val mirror: ArrayList<String> = arrayListOf()
)
