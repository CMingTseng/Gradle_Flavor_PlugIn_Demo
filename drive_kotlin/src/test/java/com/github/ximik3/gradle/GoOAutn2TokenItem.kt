package com.github.ximik3.gradle

import com.google.gson.annotations.SerializedName

data class GoOAutn2TokenItem(
        @SerializedName("accessToken")
        val accessToken: String = "",

        @SerializedName("businessIDs")
        val businessIDs: String = "",

        @SerializedName("expiredIn")
        val expiredIn: Int = 216000,

        @SerializedName("refreshExpiresIn")
        val refreshExpiresIn: Int = 432000,

        @SerializedName("refreshToken")
        val refreshToken: String = "",

        @SerializedName("tokenType")
        val tokenType: String = ""

)