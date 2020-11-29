package com.github.ximik3.gradle.cloud

import com.google.gson.annotations.SerializedName

data class Credential(
        @SerializedName("access_token")
        internal var accessToken: String? = null,

        @SerializedName("token_type")
        internal var tokenType: String? = null,

        @SerializedName("expires_in")
        internal var expiresIn: Int? = null
) {
    fun getAuthentication(): String = "$tokenType $accessToken"
}