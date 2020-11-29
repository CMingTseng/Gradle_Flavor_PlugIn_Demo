package com.github.ximik3.gradle.cloud

import com.google.gson.annotations.SerializedName

data class ResultItem(

        @SerializedName("code")
        val code: String = "",

        @SerializedName("message")
        val message: String = ""
)
