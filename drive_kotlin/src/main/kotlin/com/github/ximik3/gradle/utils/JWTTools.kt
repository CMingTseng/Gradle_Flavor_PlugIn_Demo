package com.github.ximik3.gradle.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import java.io.InputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateKey
import java.util.*

private const val ACCESS_TOKEN_EXPIRE_TIME_SEC = 60 * 60L

//TODO  Ref : https://github.com/auth0/java-jwt
//https://www.codeproject.com/Articles/1253786/Java-JWT-Token-Tutorial-using-JJWT-Library
//https://www.example-code.com/java/jwt_ecc_create.asp
//https://stackoverflow.com/questions/32019322/howto-create-googlecredential-by-using-service-account-json
//https://developers.google.com/identity/protocols/oauth2/service-account
fun getP12FilePrivateKey(credentialStream: InputStream): PrivateKey {
    val keyStore = KeyStore.getInstance("PKCS12")
    val password = "notasecret".toCharArray()
    keyStore.load(credentialStream, password)
    return keyStore.getKey(keyStore.aliases().nextElement(), password) as PrivateKey
}

fun getGoogleeJSONFilePrivateKey(credentialStream: InputStream): PrivateKey {
    val credential = GoogleCredential.fromStream(credentialStream)
//        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
//                .setJsonFactory(JSON_FACTORY)
//                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
//                .setServiceAccountScopes(Collections.singleton(PlusScopes.PLUS_ME))
//                .setServiceAccountPrivateKeyFromP12File(new File("key.p12"));
    return credential.serviceAccountPrivateKey as PrivateKey
}

fun generateGoogleAuthJWT(
        serviceAccountEmail: String, scope: String, audience: String, privatekey: PrivateKey
): String {
////        iss (issuer)
////        sub (subject)
////        aud (audience)
////        iat (issued at)
////        exp (expiration time)
//        Map<String, Object> headers = new TreeMap<>();
//        headers.put(PublicClaims.ALGORITHM, "RS256");
//        headers.put(PublicClaims.TYPE, "JWT");
//        System.out.println("Show  GSON　JWTHeader :  " + sGson.toJson(headers));
//        Map<String, Object> claimSet = new TreeMap<>();
//        claimSet.put(PublicClaims.ISSUER, "for-deploy-apk@deployapk-1602571937321.iam.gserviceaccount.com");
//        claimSet.put("scope", scope);
//        claimSet.put(PublicClaims.AUDIENCE, "https://oauth2.googleapis.com/token");
//        claimSet.put(PublicClaims.ISSUED_AT, new Date(currentTimeMillis));
//        claimSet.put(PublicClaims.EXPIRES_AT, new Date(currentTimeMillis + ACCESS_TOKEN_EXPIRE_TIME_SEC));
//        System.out.println("Show  GSON　claimSet :  " + sGson.toJson(claimSet));

//        JsonWebSignature.Header header = new JsonWebSignature.Header();
//        JsonWebSignature.signUsingRsaSha256.
    val currentTimeMillis = System.currentTimeMillis()
    val algorithm = Algorithm.RSA256(null, privatekey as RSAPrivateKey)
//        String builder = JWT.create().withHeader(headers)
////                .withClaim(PublicClaims.ISSUER, serviceAccountEmail)
////                .withClaim("scope", scope)
////                .withClaim(PublicClaims.AUDIENCE, audience)
////                .withClaim(PublicClaims.EXPIRES_AT, currentTimeSeconds+ACCESS_TOKEN_EXPIRE_TIME_SEC)
////                .withClaim(PublicClaims.ISSUED_AT, currentTimeSeconds)
    return JWT.create()
            .withIssuer(serviceAccountEmail)
            .withAudience(audience)
            .withClaim("scope", scope)
            .withIssuedAt(Date(currentTimeMillis))
            .withExpiresAt(Date(currentTimeMillis + ACCESS_TOKEN_EXPIRE_TIME_SEC))
            .sign(algorithm)
}

////FIXME
//@Throws(URISyntaxException::class, IOException::class)
//fun getPrivateKey(filepath: String): PrivateKey? {
//    val url = QuickstartDrive::class.java.classLoader.getResource(filepath)
//    val f = File(url.toURI())
//    val `in` = FileInputStream(f)
//        ?: throw FileNotFoundException("Resource not found: $filepath")
//    val content = inputStreamToString(FileInputStream(f))
//    val isJSON = isJSONValid(content.toString())
//    println("Show  is JSON ? $isJSON")
//    val privatekey: PrivateKey
//    privatekey = if (isJSON) {
//        val credential = GoogleCredential.fromStream(
//            FileInputStream(f)
//        )
//        credential.serviceAccountPrivateKey
//    } else {
//        getP12FilePrivateKey(FileInputStream(f))
//    }
//    return privatekey
//}