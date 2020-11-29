package com.github.ximik3.gradle.task

import com.github.ximik3.gradle.cloud.gd.api.GoogleDriveApi
import com.github.ximik3.gradle.cloud.gd.api.GoogleOAuthApi
import com.github.ximik3.gradle.cloud.gd.api.uploadFile
import com.github.ximik3.gradle.utils.ResourceLoader
import com.github.ximik3.gradle.utils.generateGoogleAuthJWT
import com.github.ximik3.gradle.utils.getP12FilePrivateKey
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TEST_PARENT_FOLDER_ID = "1BLsjBuvKIuXg96hvWwB4EkJ32ZWakAZi"
private const val UPLOAD_FILE_SAMPLE = "app-demo1-dev_debug.apk"
private const val CREDENTIALS_FILE_PATH = "deployapk-1602571937321-a59632391db8.p12"
private val gson = GsonBuilder().setPrettyPrinting().create()
val httpClient = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
        )
).build()


//private const val ACCESS_TOKEN_EXPIRE_TIME_SEC = 60 * 60L

//private fun getP12FilePrivateKey(credentialStream: InputStream): PrivateKey {
//    val keyStore = KeyStore.getInstance("PKCS12")
//    val password = "notasecret".toCharArray()
//    keyStore.load(credentialStream, password)
//    return keyStore.getKey(keyStore.aliases().nextElement(), password) as PrivateKey
//}
//
//private fun generateGoogleAuthJWT(
//    serviceAccountEmail: String, scope: String, audience: String, privatekey: PrivateKey
//): String {
//    val currentTimeMillis = System.currentTimeMillis()
//    val algorithm = Algorithm.RSA256(null, privatekey as RSAPrivateKey)
//    return JWT.create()
//        .withIssuer(serviceAccountEmail)
//        .withAudience(audience)
//        .withClaim("scope", scope)
//        .withIssuedAt(Date(currentTimeMillis))
//        .withExpiresAt(Date(currentTimeMillis + ACCESS_TOKEN_EXPIRE_TIME_SEC))
//        .sign(algorithm)
//}

inline fun <reified T> createService(
        baseUrl: String,
        converterFactory: Converter.Factory = GsonConverterFactory.create()
): T {
    return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(httpClient)
            .build()
            .create(T::class.java)
}

fun main() {
    val testFile =
            ResourceLoader.getResourceFile(UPLOAD_FILE_SAMPLE) ?: throw IllegalArgumentException(
                    "no such file"
            )
    val scope = "https://www.googleapis.com/auth/drive"
    val audience = "https://accounts.google.com/o/oauth2/token"
    var accountId = "for-deploy-apk@deployapk-1602571937321.iam.gserviceaccount.com"

    val privateKey = ResourceLoader.openStream(CREDENTIALS_FILE_PATH).use {
        getP12FilePrivateKey(it)
    }
    val jwt = generateGoogleAuthJWT(accountId, scope, audience, privateKey)

    val googleOAuthApi = createService<GoogleOAuthApi>("https://oauth2.googleapis.com/")
    val googleDriveApi = createService<GoogleDriveApi>(
            "https://www.googleapis.com/",
            ScalarsConverterFactory.create()
    )
    //fixme jwt_type
    val credential = googleOAuthApi.fetchToken("", jwt).execute().body()!!
    println("Show  JWT credential  :  $credential")
    googleDriveApi.uploadFile(
            httpClient,
            credential,
            testFile,
            UPLOAD_FILE_SAMPLE,
            TEST_PARENT_FOLDER_ID
    )
}
