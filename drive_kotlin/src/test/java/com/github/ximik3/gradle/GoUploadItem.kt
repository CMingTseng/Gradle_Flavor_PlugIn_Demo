package com.github.ximik3.gradle

import com.google.gson.annotations.SerializedName

data class GoUploadItem(
        @SerializedName("data")
        val data: UploadItemData
)

data class UploadItemData(
        @SerializedName("url")
        val url: String

)
