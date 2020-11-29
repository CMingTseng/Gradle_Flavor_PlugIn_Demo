package com.github.ximik3.gradle;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.ximik3.gradle.cloud.api.data.response.Credential;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.stream.Stream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Google OAuth 2.0 service account authorization helper
 *
 * @see <a href="https://developers.google.com/identity/protocols/OAuth2ServiceAccount">
 * https://developers.google.com/identity/protocols/OAuth2ServiceAccount
 * @see <a href="https://reurl.cc/ygK7pE">
 * https://reurl.cc/ygK7pE
 * </a>
 * Created by volodymyr.kukhar on 10/24/16.
 */
public class AuthorizationHelper {
    private static final Long ACCESS_TOKEN_EXPIRE_TIME_SEC = 60 * 60L;      // + 60 minutes

    protected static Credential authorize(DrivePluginExtension extension) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, SignatureException, InvalidKeyException, URISyntaxException {
        final File certificateFile = new File(extension.certificateFile);
        if (certificateFile.exists()) {
            PrivateKey privatekey;
//            String scope = extension.fscopes
            String scope = "https://www.googleapis.com/auth/drive";
            String accountId = extension.account;
            return authorizeWithServiceAccount(accountId, scope, certificateFile);
        }
        throw new IllegalArgumentException("No credentials provided.");
    }


    /**
     * Authorization
     *
     * @param serviceAccountEmail
     * @param certificateFile
     * @return
     * @throws IOException
     * @see <a href="https://documenter.getpostman.com/view/8140651/SWECYFyf">
     * https://documenter.getpostman.com/view/8140651/SWECYFyf
     * @see <a href="https://stackoverflow.com/questions/43650859/not-getting-access-token-using-retrofit2-in-android-client">
     * https://stackoverflow.com/questions/43650859/not-getting-access-token-using-retrofit2-in-android-client
     * @see <a href=" https://juejin.im/post/6844904085112487950">
     * https://juejin.im/post/6844904085112487950
     * @see <a href=" https://www.muleblogs.com/authenticating-with-google-drive-in-mule-part-2-jwt/">
     * https://www.muleblogs.com/authenticating-with-google-drive-in-mule-part-2-jwt/
     * @see <a href="https://www.ptt.cc/bbs/Ajax/M.1353137591.A.0F9.html">
     * https://www.ptt.cc/bbs/Ajax/M.1353137591.A.0F9.html
     * @see <a href="https://reurl.cc/e8VDeb">
     * https://reurl.cc/e8VDeb
     * @see <a href="https://cloud.google.com/endpoints/docs/openapi/authenticating-users-auth0?hl=zh-tw">
     * https://cloud.google.com/endpoints/docs/openapi/authenticating-users-auth0?hl=zh-tw
     * </a>
     */
    private static Credential authorizeWithServiceAccount(String serviceAccountEmail, String scope, File certificateFile) throws IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, InvalidKeyException, SignatureException, URISyntaxException {
        final String audience = "https://accounts.google.com/o/oauth2/token";
        Credential credential = null;
        PrivateKey privatekey = getPrivateKey(certificateFile);
        String JWT = generateGoogleAuthJWT(serviceAccountEmail, scope, audience, privatekey);
        System.out.println("Show JWT  :  " + JWT);
        if (!JWT.isEmpty()) {
            /**
             *  Use HttpURLConnection
             *               URL authUrl = new URL("https://oauth2.googleapis.com/token");
             *             HttpURLConnection auth = (HttpURLConnection) authUrl.openConnection();
             *             auth.setRequestMethod("POST");
             *             auth.setRequestProperty("Host", "oauth2.googleapis.com");
             *             auth.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             *             auth.setDoOutput(true);
             *             String body = "grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer&assertion=" + JWT;
             *             auth.getOutputStream().write(body.getBytes());
             *             BufferedReader reader = new BufferedReader(new InputStreamReader(auth.getInputStream()));
             *             StringBuilder sb = new StringBuilder();
             *             for (int c; (c = reader.read()) >= 0; ) {
             *                 sb.append((char) c);
             *             }
             *             System.out.println("Show  result  :  " + sb.toString());
             *             AccessToken accessToken = new Gson().fromJson(sb.toString(), AccessToken.class);
             */
            /**
             *  Use OKHttp !!!
             *             OkHttpClient client = new OkHttpClient().newBuilder()
             *                                  .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
             *                                   .build();
             *             MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
             *             RequestBody requestbody = RequestBody.create(mediaType, "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion="+ JWT);
             *             Request request = new Request.Builder()
             *                     .url("https://oauth2.googleapis.com/token")
             *                     .method("POST", requestbody)
             *                     .addHeader("Content-Type", "application/x-www-form-urlencoded")
             *                     .build();
             *             Response response = client.newCall(request).execute();
             */
            /**
             *  Use OKHttp Interceptor!!!
             *             OkHttpClient client = new OkHttpClient().newBuilder()
             *                     .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
             *                     .addInterceptor(new Interceptor() {
             *                         @Override
             *                         public okhttp3.Response intercept(Chain chain) throws IOException {
             *                             Request original = chain.request();
             *                             Request.Builder requestBuilder = original.newBuilder().addHeader("ContentType", "application/x-www-form-urlencoded")
             *                                     .post(new FormBody.Builder()
             *                                             .add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
             *                                             .add("assertion", JWT)
             *
             *                                     );
             *                             Request request = requestBuilder.build();
             *                             return chain.proceed(request);
             *                         }
             *                     }) .build();
             *             Request request = new Request.Builder() .url("https://oauth2.googleapis.com/token") .build();
             *             Response response = client.newCall(request).execute();
             */
            /**
             *  Use OKHttp Interceptor !!!
             *             OkHttpClient client = new OkHttpClient().newBuilder()
             *                     .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
             *                     .addInterceptor(new Interceptor() {
             *                         @Override
             *                         public okhttp3.Response intercept(Chain chain) throws IOException {             *
             *                             Request original = chain.request();
             *                             MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
             *                             RequestBody requestbody = RequestBody.create(mediaType, "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=" + JWT);             *
             *                             Request.Builder requestBuilder = original.newBuilder().addHeader("ContentType", "application/x-www-form-urlencoded").method("POST", requestbody);
             *                             Request request = requestBuilder.build();
             *                             return chain.proceed(request);
             *                         }
             *                     })
             *                     .build();
             *             Request request = new Request.Builder() .url("https://oauth2.googleapis.com/token") .build();
             *             Response response = client.newCall(request).execute();
             */
            /**
             *  Use Retrofit !!!
             *             OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();
             *             Retrofit retrofit = new Retrofit.Builder()
             *                     .baseUrl("https://oauth2.googleapis.com/")
             *                     .addConverterFactory(GsonConverterFactory.create())
             *                     .client(okHttpClient)
             *                     .build();
             *             GDApiService service = retrofit.create(GDApiService.class);
             *             Call<AccessToken> fetchtoken = service.fetchToken("urn:ietf:params:oauth:grant-type:jwt-bearer", JWT);
             *             fetchtoken.enqueue(new Callback<AccessToken>() {
             *                 @Override
             *                 public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
             *                     System.out.println("Show  response  :  " + response.body().toString());
             *                 }
             *
             *                 @Override
             *                 public void onFailure(Call<AccessToken> call, Throwable t) {
             *
             *                 }
             *             });
             */


            Request request = new Request.Builder()
                    .url("https://oauth2.googleapis.com/token")
                    .method("POST", RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=" + JWT))
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = new OkHttpClient().newBuilder().build().newCall(request).execute();
            credential = new Gson().fromJson(response.body().toString(), Credential.class);
        } else {
            throw new IllegalArgumentException("No JWT provided.");
        }
        return credential;
    }

    private final static boolean isJSONValid(String jsonInString) {
        try {
            new Gson().fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    private static final String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private static String readAllBytesJava7(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private static String readAllBytesJava6usingBufferedReader(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private static final String inputStreamToString(InputStream stream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8);
        int charsRead;
        while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        return out.toString();
    }

    /**
     * @see <a href="https://github.com/auth0/java-jwt">
     * https://github.com/auth0/java-jwt
     * @see <a href="https://www.codeproject.com/Articles/1253786/Java-JWT-Token-Tutorial-using-JJWT-Library">
     * https://www.codeproject.com/Articles/1253786/Java-JWT-Token-Tutorial-using-JJWT-Library
     * @see <a href="https://www.example-code.com/java/jwt_ecc_create.asp">
     * https://www.example-code.com/java/jwt_ecc_create.asp
     * @see <a href="https://stackoverflow.com/questions/32019322/howto-create-googlecredential-by-using-service-account-json">
     * https://stackoverflow.com/questions/32019322/howto-create-googlecredential-by-using-service-account-json
     * @see <a href="https://developers.google.com/identity/protocols/oauth2/service-account">
     * https://developers.google.com/identity/protocols/oauth2/service-account
     * @see <a href="https://documenter.getpostman.com/view/8140651/SWECYFyf">
     * https://documenter.getpostman.com/view/8140651/SWECYFyf
     * @see <a href="https://stackoverflow.com/questions/43650859/not-getting-access-token-using-retrofit2-in-android-client">
     * https://stackoverflow.com/questions/43650859/not-getting-access-token-using-retrofit2-in-android-client
     * @see <a href=" https://juejin.im/post/6844904085112487950">
     * https://juejin.im/post/6844904085112487950
     * @see <a href=" https://www.muleblogs.com/authenticating-with-google-drive-in-mule-part-2-jwt/">
     * https://www.muleblogs.com/authenticating-with-google-drive-in-mule-part-2-jwt/
     * @see <a href="https://www.ptt.cc/bbs/Ajax/M.1353137591.A.0F9.html">
     * https://www.ptt.cc/bbs/Ajax/M.1353137591.A.0F9.html
     * @see <a href="https://reurl.cc/e8VDeb">
     * https://reurl.cc/e8VDeb
     * @see <a href="https://cloud.google.com/endpoints/docs/openapi/authenticating-users-auth0?hl=zh-tw">
     * https://cloud.google.com/endpoints/docs/openapi/authenticating-users-auth0?hl=zh-tw
     * @see <a href="https://reurl.cc/e8VDeb">
     * https://reurl.cc/e8VDeb
     * @see <a href=" https://stackoverflow.com/questions/359472/how-can-i-verify-a-google-authentication-api-access-token">
     * https://stackoverflow.com/questions/359472/how-can-i-verify-a-google-authentication-api-access-token
     * @see <a href=" https://github.com/auth0/JWTDecode.Android">
     * https://github.com/auth0/JWTDecode.Android
     * @see <a href=" https://blog.csdn.net/mythmayor/article/details/81221642">
     * https://blog.csdn.net/mythmayor/article/details/81221642
     * @see <a href="https://github.com/clouway/jwt-java-client">
     * https://github.com/clouway/jwt-java-client
     * </a>
     */
    private static String generateGoogleAuthJWT(String serviceAccountEmail, String scope, String audience, PrivateKey privatekey) {
        final long currentTimeMillis = System.currentTimeMillis();
        /**
         *  //TODO  JWT is   Base64(Header)+"."+Base64(Payload_Claims)+"."+Base64(Signature)
         *        iss (issuer)
         *        sub (subject)
         *        aud (audience)
         *        iat (issued at)
         *        exp (expiration time)
         */
        /**
         *    Use manual !!!
         *        String JWTHeader = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
         *        String JWTClaimSet = "{\\n' +\n" +
         *               '  \"iss\":' + serviceAccountEmail + ',\\n' +\n" +
         *               '  \"scope\":\"https://www.googleapis.com/auth/drive\",\\n' +\n" +
         *               '  \"aud\":\"https://www.googleapis.com/oauth2/v4/token\",\\n' +\n" +
         *               '  \"exp\":' + (System.currentTimeSeconds() + ACCESS_TOKEN_EXPIRE_TIME_SEC) + ',\\n' +\n" +
         *               '  \"iat\":' + System.currentTimeSeconds() + '\\n' +\n" +
         *               '}";
         *        String JWT = Base64.encodeBase64URLSafeString(JWTHeader.getBytes()) + '.' + Base64.encodeBase64URLSafeString(JWTClaimSet.getBytes());
         *        Signature signature = Signature.getInstance("SHA256withRSA");
         *        signature.initSign(privatekey);
         *        signature.update(JWT.getBytes());
         *        JWT += '.' + Base64.encodeBase64URLSafeString(signature.sign());
         */
        /**
         /**
         *    Use Keymap & gson
         *         final Map<String, Object> headers = new TreeMap<>();
         *         headers.put(PublicClaims.ALGORITHM, "RS256");
         *         headers.put(PublicClaims.TYPE, "JWT");
         *         System.out.println("Show  GSON　JWTHeader :  " + sGson.toJson(headers));
         *         Map<String, Object> claimSet = new TreeMap<>();
         *         claimSet.put(PublicClaims.ISSUER, "for-deploy-apk@deployapk-1602571937321.iam.gserviceaccount.com");
         *         claimSet.put("scope", scope);
         *         claimSet.put(PublicClaims.AUDIENCE, "https://oauth2.googleapis.com/token");
         *         claimSet.put(PublicClaims.ISSUED_AT, new Date(currentTimeMillis));
         *         claimSet.put(PublicClaims.EXPIRES_AT, new Date(currentTimeMillis + ACCESS_TOKEN_EXPIRE_TIME_SEC));
         *         System.out.println("Show  GSON　claimSet :  " + sGson.toJson(claimSet));
         */
        /**
         *    Use google library
         *         JsonWebSignature.Header header = new JsonWebSignature.Header()
         *        JsonWebSignature.signUsingRsaSha256.
         */
        /**
         *    Use JWT library
         *    Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) privatekey);
         *         String builder = JWT.create()
         *                          .withHeader(headers)
         *                          .withClaim(PublicClaims.ISSUER, serviceAccountEmail)
         *                          .withClaim("scope", scope)
         *                          .withClaim(PublicClaims.AUDIENCE, audience)
         *                          .withClaim(PublicClaims.ISSUED_AT, currentTimeSeconds)
         *                          .withClaim(PublicClaims.EXPIRES_AT, currentTimeSeconds+ACCESS_TOKEN_EXPIRE_TIME_SEC)
         *         String builder = JWT.create()
         *                          .withIssuer(serviceAccountEmail)
         *                          .withAudience(audience)
         *                          .withClaim("scope", "https://www.googleapis.com/auth/drive")
         *                          .withIssuedAt(new Date(currentTimeMillis))
         *                          .withExpiresAt(new Date(currentTimeMillis + ACCESS_TOKEN_EXPIRE_TIME_SEC)).sign(algorithm);
         */

        //TODO use JWT library
        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) privatekey);
        String builder = JWT.create()
                .withIssuer(serviceAccountEmail)
                .withAudience(audience)
                .withClaim("scope", scope)
                .withIssuedAt(new Date(currentTimeMillis))
                .withExpiresAt(new Date(currentTimeMillis + ACCESS_TOKEN_EXPIRE_TIME_SEC)).sign(algorithm);
        return builder;

    }


    private static PrivateKey getPrivateKey(File f) throws URISyntaxException, IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        final InputStream in = new FileInputStream(f);
        if (in == null) {
            throw new FileNotFoundException("credentials info lost");
        }
        final String content = inputStreamToString(new FileInputStream(f));
        boolean isJSON = isJSONValid(content.toString());
        System.out.println("Show  is JSON ? " + isJSON);
        PrivateKey privatekey;
        if (isJSON) {
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(f));
            privatekey = credential.getServiceAccountPrivateKey();
        } else {
            privatekey = getP12FilePrivateKey(new FileInputStream(f));
        }
        return privatekey;
    }

    private static PrivateKey getP12FilePrivateKey(InputStream credentialStream) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        char[] keystorepasswordchar = "notasecret".toCharArray();
        keyStore.load(credentialStream, keystorepasswordchar);
        return (PrivateKey) keyStore.getKey(keyStore.aliases().nextElement(), keystorepasswordchar);
    }
}
