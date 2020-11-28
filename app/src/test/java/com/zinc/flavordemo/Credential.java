package com.zinc.flavordemo;

import com.google.gson.annotations.SerializedName;

public class Credential {
    @SerializedName("access_token")
    String accessToken;

    @SerializedName("token_type")
    String tokenType;

    @SerializedName("expires_in")
    Integer expiresIn;

    public String getAuthentication() {
        return tokenType + " " + accessToken;
    }
}

//kotlin version

//data class Credential(
//        @SerializedName("access_token")
//        internal var accessToken: String? = null,
//
//        @SerializedName("token_type")
//        internal var tokenType: String? = null,
//
//        @SerializedName("expires_in")
//        internal var expiresIn: Int? = null
//)
//{
//    fun getAuthentication(): String = "$tokenType $accessToken"
//}

