package com.github.ximik3.gradle

import com.google.gson.annotations.SerializedName

data class GoLoginRequest(
        @SerializedName("deviceID")
        val deviceID: String = "",

        @SerializedName("userID")
        val userID: String = "",

        @SerializedName("username")
        val username: String = "",

        @SerializedName("password")
        val password: String = "",

        @SerializedName("userType")
        val userType: Int = 4,

        @SerializedName("grant_type")
        val appID: String = ""

)