package com.example.myapplication

import com.zinc.flavordemo.Credential
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleOAuthApi {
    @FormUrlEncoded
    @POST("token")
    fun fetchToken(
            @Field("grant_type") jwt_type: String,
            @Field("assertion") jwt: String
    ): Call<Credential>
}

fun GoogleOAuthApi.fetchToken(jwt: String): Call<Credential> =
        fetchToken("urn:ietf:params:oauth:grant-type:jwt-bearer", jwt)