package com.zinc.flavordemo;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;

import com.example.myapplication.GoogleDriveApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.JWTToolsKt.generateGoogleAuthJWT;
import static com.example.myapplication.JWTToolsKt.getP12FilePrivateKey;
import static com.example.myapplication.StringToolKt.inputStreamToString;
import static com.example.myapplication.StringToolKt.isJSONValid;


public class QuickstartDrive {
    private static String CREDENTIALS_FILE_PATH = "deployapk-1602571937321-a59632391db8.p12";
//    private static String CREDENTIALS_FILE_PATH = "deployapk-1602571937321-96dabfa3844d.json";

    private static final Long ACCESS_TOKEN_EXPIRE_TIME_SEC = 5 * 60L;      // + 60 minutes Max is 3600
    private static final int RESUMABLE_UPLOAD_PART_MAX_LENGTH = 256 * 1024;     // bytes
    private static final Gson sGson = new Gson();
    private static Credential accessToken;
    //    private static final OkHttpClient.Builder mClientb = new OkHttpClient.Builder();
    private static final GoogleDriveUploadItem mInfos = new GoogleDriveUploadItem();

    //FIXME Ref : https://juejin.im/post/6844904085112487950
    //
    public static void main(String... args) throws IOException, GeneralSecurityException, URISyntaxException {
        //FIXME  WTF !!  why  use "./deployapk-1602571937321-a59632391db8.p12" check is pass  use "/deployapk-1602571937321-a59632391db8.p12" fail ?
        //    but GoogleCredential.fromStream /  getP12FilePrivateKey must  "/deployapk-1602571937321-a59632391db8.p12"
//        InputStream in = ClassLoaderUtil.getResourceAsStream(CREDENTIALS_FILE_PATH,QuickstartDrive.class);
        URL url = QuickstartDrive.class.getClassLoader().getResource(CREDENTIALS_FILE_PATH);
        File f = new File(url.toURI());
        final InputStream in = new FileInputStream(f);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        final String content = inputStreamToString(new FileInputStream(f));
//        final String content = readAllBytesJava6usingBufferedReader(CREDENTIALS_FILE_PATH);//fail can not found file !!???
//        System.out.println("Show  is JSON ? " + isJSONValid(content));
        String JWT = "";
        PrivateKey privatekey;
        String scope = "https://www.googleapis.com/auth/drive";
        String audience = "https://accounts.google.com/o/oauth2/token";
        String accountId = "for-deploy-apk@deployapk-1602571937321.iam.gserviceaccount.com";

        if (isJSONValid(content)) {
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(f));
//            //        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
////                .setJsonFactory(JSON_FACTORY)
////                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
////                .setServiceAccountScopes(Collections.singleton(PlusScopes.PLUS_ME))
////                .setServiceAccountPrivateKeyFromP12File(new File("key.p12"));
            privatekey = credential.getServiceAccountPrivateKey();
            accountId = credential.getServiceAccountId();
        } else {
            privatekey = getP12FilePrivateKey(new FileInputStream(f));
//            Map<String, Object> headers = new TreeMap<>();
//            headers.put(PublicClaims.ALGORITHM, "RS256");
//            headers.put(PublicClaims.TYPE, "JWT");
//            System.out.println("Show  GSON　JWTHeader :  " +  sGson.toJson(headers));
//
//            //FIXME
//
//            Map<String, Object> claimSet = new TreeMap<>();
//            claimSet.put(PublicClaims.ISSUER, "for-deploy-apk@deployapk-1602571937321.iam.gserviceaccount.com");
//            claimSet.put("scope", "https://www.googleapis.com/auth/drive");
//            claimSet.put(PublicClaims.AUDIENCE, "https://oauth2.googleapis.com/token");
////        claimSet.put(PublicClaims.EXPIRES_AT,new Date(currentTimeMillis+ ACCESS_TOKEN_EXPIRE_TIME_SEC) );
////        claimSet.put(PublicClaims.ISSUED_AT, new Date(currentTimeMillis));
//            claimSet.put(PublicClaims.EXPIRES_AT,currentTimeMillis+ ACCESS_TOKEN_EXPIRE_TIME_SEC );
//            claimSet.put(PublicClaims.ISSUED_AT, currentTimeMillis);
//            System.out.println("Show  GSON　claimSet :  " +  sGson.toJson(claimSet));
//
//            String JWTs = Base64.encodeBase64URLSafeString(sGson.toJson(headers).getBytes()) +
//                    '.' + Base64.encodeBase64URLSafeString(sGson.toJson(claimSet).getBytes());
//            Signature signature = Signature.getInstance("SHA256withRSA");
//            signature.initSign(privatekey);
//            signature.update(JWTs.getBytes());
//            JWTs += '.' + Base64.encodeBase64URLSafeString(signature.sign());
//            System.out.println("Show  JWT  :  " + JWTs);

            //        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
//                .setJsonFactory(JSON_FACTORY)
//                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
//                .setServiceAccountScopes(Collections.singleton(PlusScopes.PLUS_ME))
//                .setServiceAccountPrivateKeyFromP12File(new File("key.p12"));
//
//        HttpTransport httpTransport= GoogleNetHttpTransport.newTrustedTransport();
//         final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//        GoogleCredential.Builder b = new GoogleCredential.Builder().setTransport(httpTransport)
//                .setJsonFactory(JSON_FACTORY).setServiceAccountId("for-deploy-apk@deployapk-1602571937321.iam.gserviceaccount.com")
//                .setServiceAccountPrivateKey(SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(),
//                        new FileInputStream(CREDENTIALS_FILE_PATH), "notasecret",
//                        "privatekey", "notasecret"))
//                .setServiceAccountScopes(GOOGLE_SCOPE_LIST);
//
//        GoogleCredential credential = b.build();
        }
        JWT = generateGoogleAuthJWT(accountId, scope, audience, privatekey);
//        System.out.println("Show  JWT  :  " + JWT);
        if (!JWT.isEmpty()) {
            //             String JWT_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:jwt-bearer";
            //PASS -- HttpURLConnection
//            URL authUrl = new URL("https://oauth2.googleapis.com/token");
//            HttpURLConnection auth = (HttpURLConnection) authUrl.openConnection();
//            auth.setRequestMethod("POST");
//            auth.setRequestProperty("Host", "oauth2.googleapis.com");
//            auth.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            auth.setDoOutput(true);
//            String body = "grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer&assertion=" + JWT;
//            auth.getOutputStream().write(body.getBytes());//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(auth.getInputStream()));
//            StringBuilder sb = new StringBuilder();
//            for (int c; (c = reader.read()) >= 0; ) {
//                sb.append((char) c);
//               }
//            System.out.println("Show  result  :  " + sb.toString());
//            AccessToken accessToken = new Gson().fromJson(sb.toString(), AccessToken.class);
//            String body2 = "urn:ietf:params:oauth:grant-type:jwt-bearer=wt-bearer&assertion=" + JWT;
            //PASS  -- pure okhttp


            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody requestbody = RequestBody.create(mediaType, "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=" + JWT);
            Request request = new Request.Builder()
                    .url("https://oauth2.googleapis.com/token")
                    .method("POST", requestbody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            okhttp3.Response response = client.newCall(request).execute();
            System.out.println("Show  result  :  " + response.body().string());

//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public okhttp3.Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .addHeader("ContentType", "application/x-www-form-urlencoded");
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }
//            });


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://oauth2.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build();
            GDApiService service = retrofit.create(GDApiService.class);
            Call<Credential> fetchtoken = service.fetchToken("https://oauth2.googleapis.com/token", "urn:ietf:params:oauth:grant-type:jwt-bearer", JWT);
            fetchtoken.enqueue(new Callback<Credential>() {
                @Override
                public void onResponse(Call<Credential> call, Response<Credential> response) {
//                    System.out.println("Show  response  tokenType :  " + response.body().tokenType);
//                    System.out.println("Show  response Token :  " + response.body().accessToken);
                    accessToken = response.body();

                    // Ref : https://stackoverflow.com/questions/39887303/resumable-upload-in-drive-rest-api-v3/48846988
                    URL url = QuickstartDrive.class.getClassLoader().getResource("app-demo1-dev_debug.apk");
                    File f = null;
                    try {
                        f = new File(url.toURI());
//                        System.out.println("Show  File :  " + f.exists());
                        if (f.exists()) {
                            long fileSize = f.length();
                            mInfos.mFileName = f.getName();
                            System.out.println("Show  File name :  " + f.getName());
                            mInfos.mFileTotalSize = fileSize;
                            mInfos.addFolderIds("1BLsjBuvKIuXg96hvWwB4EkJ32ZWakAZi");
//                            System.out.println("Show  now upload info  :  " + sGson.toJson(mInfos));
                            //pure okhttp
//                            OkHttpClient.Builder clientb = new OkHttpClient.Builder();
////                            clientb.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
//                            RequestBody requestbody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sGson.toJson(mInfos));
//                            Request step1_request = new Request.Builder()
//                                    .url("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable")
//                                    .method("POST", requestbody)
//                                    .addHeader("Authorization", accessToken.tokenType + " " + accessToken.accessToken)
//                                    .addHeader("Content-Type", "application/json; charset=UTF-8")
//                                    .addHeader("X-Upload-Content-Type", "application/octet-stream")
//                                    .addHeader("X-Upload-Content-Length", String.valueOf(mInfos.mFileTotalSize))
//                                    .build();
//
//                            try {
//                                okhttp3.Response preresponse = clientb.build().newCall(step1_request).execute();
//                                int code = preresponse.code();
//                                Set<String> headers = null;
//                                if (preresponse.headers() != null) {
//                                    headers = preresponse.headers().names();
//                                }
//                                if (code == 200) {
//                                    for (String s : headers) {
//                                        if (s.equals("Location")) {
//                                            String location = preresponse.header(s);
//                                            mInfos.mFileUploadUrl = location;
//                                            String fileid = location.split("upload_id=")[1];
//                                            mInfos.mFileUploadId = fileid;
//                                        }
//                                    }
//                                    System.out.println("Show  now sync upload info  :  " + sGson.toJson(mInfos));
//                                    long totalSize = f.length();
//
//                                    long lastByteCount = 0L;
//                                    BufferedInputStream inputStream = getInputStream(f);
//                                    while (lastByteCount != totalSize) {
//                                        final byte[] RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY = new byte[RESUMABLE_UPLOAD_PART_MAX_LENGTH];
//                                        int datatranslength = readFileMetadata(inputStream, RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY);
//                                        System.out.println("Show  upload !!!!!    lastByteCount : "+lastByteCount);
//                                        System.out.println("Show  upload !!!!!    datatranslength : "+datatranslength);
//                                        if (datatranslength == 0) {
//                                            System.out.println("Show  upload !!!!!    datatranslength  ==0 : ");
//                                            break;
//                                        }
//                                        RequestBody filebody = RequestBody.create(null, RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY, 0, datatranslength);
//                                        String contentrange = "bytes " + lastByteCount + "-" + (lastByteCount + datatranslength - 1) + "/" + totalSize;
//                                        System.out.println("Show  Content-Range  : "+contentrange);
//                                        Request file_put_request = new Request.Builder()
//                                                .url(mInfos.mFileUploadUrl)
//                                                .method("PUT", filebody)
//                                                .addHeader("Authorization", accessToken.tokenType + " " + accessToken.accessToken)
//                                                .addHeader("Content-Length", String.valueOf(datatranslength))
//                                                .addHeader("Content-Range", contentrange)
//                                                .build();
//                                        System.out.println("--------------------------------------------------------------");
//                                        okhttp3.Response fileresponse = clientb.build().newCall(file_put_request).execute();
//                                        code = fileresponse.code();
//                                        if (code == 200) {
//                                            break;
//                                        } else if (code == 308) {
//                                            if (fileresponse.headers() != null) {
//                                                headers = fileresponse.headers().names();
//                                            }
//                                            for (String s : headers) {
////                                                Range  bytes=0-262143
//                                                if (s.equals("Range")) {
//                                                    String range = fileresponse.header(s);
//                                                    long nextPosition = Long.valueOf(range.split("=")[1].split("-")[1]) + 1;
//                                                    if (lastByteCount + datatranslength != nextPosition) {
//                                                        inputStream.close();
//                                                        inputStream = getInputStream(f);
//                                                        inputStream.skip(nextPosition);
//                                                    } else {
//                                                        System.out.println("-match");
//                                                        nextPosition = lastByteCount + Long.valueOf(datatranslength);
//                                                    }
//                                                    lastByteCount = nextPosition;
//                                                }
//                                            }
//                                        } else {
//
//                                        }
//
//                                    }
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                            // okhttp +Interceptor
                            OkHttpClient.Builder clientb = new OkHttpClient.Builder();
                            File finalF = f;
                            clientb.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
                            try {
                                clientb
                                        .addInterceptor(new Interceptor() {
                                            //FIXME init
                                            BufferedInputStream inputStream = getInputStream(finalF);

                                            @Override
                                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                                long totalSize = finalF.length();
                                                Request prerequest = chain.request();
                                                okhttp3.Response preresponse = chain.proceed(prerequest);
                                                Set<String> preheaders = prerequest.headers().names();

                                                Set<String> headers = null;
                                                if (preresponse.headers() != null) {
                                                    headers = preresponse.headers().names();
                                                }
                                                //                                            System.out.println("Show  preheader method  : " + prerequest.method());
                                                //                                            for (String s : preheaders) {
                                                //                                                System.out.println("Show  preheader  :  " + s + "  " + prerequest.headers().get(s));
                                                //                                            }
                                                int code = preresponse.code();
                                                String action = prerequest.method();
                                                switch (code) {
                                                    case 200:
                                                        if (action.equals("POST")) {
                                                            System.out.println("Show  action post 200    ");
                                                            for (String s : headers) {
                                                                if (s.equals("Location")) {
                                                                    String location = preresponse.header(s);
                                                                    mInfos.mFileUploadUrl = location;
                                                                    String fileid = location.split("upload_id=")[1];
                                                                    mInfos.mFileUploadId = fileid;
                                                                }
                                                            }
                                                            System.out.println("Show  now sync upload info  :  " + sGson.toJson(mInfos));

                                                            Request file_put_request = getResumableUploadingRequest(accessToken.getAuthentication(), totalSize, inputStream, 0L);
                                                            clientb.build().newCall(file_put_request).execute();
                                                        } else if (action.equals("PUT")) {

                                                            System.out.println("Show  upload !FFFFFFFFFFFFFFFF");

                                                        }
                                                        break;
                                                    case 308:

                                                        for (String s : preheaders) {
                                                            System.out.println("Show  preheader  :  " + s + "  " + prerequest.headers().get(s));
                                                        }
                                                        System.out.println("Show  action put  308   ");
                                                        if (action.equals("PUT")) {
                                                            long lastByteCount = 0L;
                                                            byte[] RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY = new byte[RESUMABLE_UPLOAD_PART_MAX_LENGTH];

                                                            int datatranslength = readFileMetadata(inputStream, RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY);
                                                            System.out.println("Show  init  datatranslength : " + datatranslength);

                                                            for (String s : headers) {
                                                                if (s.equals("Range")) {
                                                                    String range = preresponse.header(s);
                                                                    System.out.println("Show  Range  " + range);
                                                                    long nextPosition = Long.valueOf(range.split("=")[1].split("-")[1]) + 1;
                                                                    System.out.println("Show  upload !!!!!2  nextPosition : " + nextPosition);
                                                                    if (lastByteCount + datatranslength != nextPosition) {
                                                                        inputStream.close();
                                                                        inputStream = getInputStream(finalF);
                                                                        inputStream.skip(nextPosition);
                                                                    } else {
                                                                        nextPosition = lastByteCount + Long.valueOf(datatranslength);
                                                                    }
                                                                    lastByteCount = nextPosition;
                                                                    System.out.println("Show  upload !!!!!2 F  nextPosition : " + nextPosition);
                                                                }
                                                            }
                                                            if (datatranslength == 0) {
                                                                System.out.println("Show  upload !!!!!2  datatranslength == 0 ");
                                                            }
                                                            if (lastByteCount != totalSize) {
                                                                Request file_put_request = getResumableUploadingRequest(accessToken.getAuthentication(), totalSize, inputStream, lastByteCount);
                                                                System.out.println("Show  upload work   totalSize : " + totalSize);
                                                                System.out.println("Show  upload work   lastByteCount : " + lastByteCount);
                                                                System.out.println("Show  upload work   datatranslength : " + datatranslength);
                                                                clientb.build().newCall(file_put_request).execute();
                                                            }
                                                        }
                                                        break;
                                                    case 400:

                                                        for (String s : preheaders) {
                                                            System.out.println("Show  preheader  :  " + s + "  " + prerequest.headers().get(s));
                                                        }
                                                        //400 Bad Request --- Failed to parse Content-Range header
                                                        System.out.println("Show  upload get 400!!!! : ");
                                                        break;
                                                    default:
                                                        break;
                                                }
                                                //
                                                return preresponse;
                                            }
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Request step1_request = getResumableInitRequest(accessToken.getAuthentication(), String.valueOf(f.length()));
                            try {
                                clientb.build().newCall(step1_request).execute();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


//                            // okhttp & retrofit2
//                            Call<ResponseBody> prepareupload = service.resumableUpload("https://www.googleapis.com/upload/drive/v3/files", accessToken.getAuthentication(), String.valueOf(mInfos.mFileTotalSize), "resumable");
//                            File finalF = f;
//                            prepareupload.enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                    System.out.println("Show  response  body :  " + response.body());
//                                    int code = response.code();
//                                    Set<String> headers = response.headers().names();
//                                    if (code == 200) {
//                                        for (String s : headers) {
//                                            System.out.println("Show  Header  _" + s + " " + response.headers().get(s));
//                                            if (s.equals("Location")) {
//                                                mInfos.mFileUploadUrl = response.headers().get(s);
//                                                String fileid = response.headers().get(s).split("upload_id=")[1];// or X-GUploader-UploadID
//                                                mInfos.mFileUploadId = fileid;
//                                            } else if (s.equals("X-GUploader-UploadID")) {
//                                                mInfos.mFileUploadId = response.headers().get(s);
//                                            }
//                                        }
//                                        System.out.println("Show  now sync upload info  :  " + sGson.toJson(mInfos));
//                                        long totalSize = finalF.length();
//                                        byte[] RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY = new byte[RESUMABLE_UPLOAD_PART_MAX_LENGTH];
//                                        long lastByteCount = 0L;
//                                        BufferedInputStream inputStream = null;
//                                        try {
//                                            inputStream = getInputStream(finalF);
//                                        } catch (FileNotFoundException e) {
//                                            e.printStackTrace();
//                                        }
//                                        while (lastByteCount != totalSize) {
//                                            int length = 0;
//                                            try {
//                                                length = readFileMetadata(inputStream, RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY);
//                                                if (length == 0) {
//                                                    break;
//                                                }
//                                                String range = "bytes " + lastByteCount + "-" + (lastByteCount + length - 1) + "/" + totalSize;
//                                                RequestBody filebody = RequestBody.create(null, RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY, 0, length);
//                                                Call<ResponseBody> upload = service.keepUpload("https://www.googleapis.com/upload/drive/v3/files", accessToken.getAuthentication(), String.valueOf(mInfos.mFileTotalSize), range,"resumable",mInfos.mFileUploadId,filebody);
//                                                upload.enqueue(new Callback<ResponseBody>() {
//                                                    @Override
//                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                                        int code = response.code();
//                                                        Set<String> headers = response.headers().names();
//                                                        if (code == 200) {
//                                                            upload.cancel();
//                                                        } else if (code == 308) {
//                                                            for (String s : headers) {
//                                                                if (s.equals("Range")) {
//                                                                    String range = response.headers().get(s);
//                                                                    long nextPosition = Long.valueOf(range.split("=")[1].split("-")[1]) + 1;
//                                                                    //TODO FIXME
////                                                                    if (lastByteCount + length != nextPosition) {
////                                                                        inputStream.close();
////                                                                        inputStream = getInputStream(f);
////                                                                        inputStream.skip(nextPosition);
////                                                                    } else {
////                                                                        nextPosition = lastByteCount + Long.valueOf(length);
////                                                                    }
////                                                                    lastByteCount = nextPosition;
//                                                                }
//                                                            }
//                                                        } else {
//
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                                    }
//                                                });
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                }
//                            });


                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Credential> call, Throwable t) {

                }
            });
        }
    }

//    private static okhttp3.Response executeRequest(Request request) throws IOException {
//        return mClientb.build().newCall(request).execute();
//    }

//    private final static boolean isJSONValid(String jsonInString) {
//        try {
//            new Gson().fromJson(jsonInString, Object.class);
//            return true;
//        } catch (com.google.gson.JsonSyntaxException ex) {
//            return false;
//        }
//    }

//    private static final String readLineByLineJava8(String filePath) {
//        StringBuilder contentBuilder = new StringBuilder();
//        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
//            stream.forEach(s -> contentBuilder.append(s).append("\n"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return contentBuilder.toString();
//    }
//
//    private static String readAllBytesJava7(String filePath) {
//        String content = "";
//        try {
//            content = new String(Files.readAllBytes(Paths.get(filePath)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return content;
//    }
//
//    private static String readAllBytesJava6usingBufferedReader(String filePath) {
//        StringBuilder contentBuilder = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String sCurrentLine;
//            while ((sCurrentLine = br.readLine()) != null) {
//                contentBuilder.append(sCurrentLine).append("\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return contentBuilder.toString();
//    }

//    private static final String inputStreamToString(InputStream stream) throws IOException {
//        final int bufferSize = 1024;
//        final char[] buffer = new char[bufferSize];
//        final StringBuilder out = new StringBuilder();
//        Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8);
//        int charsRead;
//        while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
//            out.append(buffer, 0, charsRead);
//        }
//        return out.toString();
//    }

//    //TODO  Ref : https://github.com/auth0/java-jwt
////https://www.codeproject.com/Articles/1253786/Java-JWT-Token-Tutorial-using-JJWT-Library
////    https://www.example-code.com/java/jwt_ecc_create.asp
//    //https://stackoverflow.com/questions/32019322/howto-create-googlecredential-by-using-service-account-json
//    // https://developers.google.com/identity/protocols/oauth2/service-account
//    private static String generateGoogleAuthJWT(String serviceAccountEmail, String scope, String audience, PrivateKey privatekey) {
//        final long currentTimeMillis = System.currentTimeMillis();
//
////        Map<String, Object> headers = new TreeMap<>();
////        headers.put(PublicClaims.ALGORITHM, "RS256");
////        headers.put(PublicClaims.TYPE, "JWT");
////        System.out.println("Show  GSON　JWTHeader :  " + sGson.toJson(headers));
//////        iss (issuer)
//////        sub (subject)
//////        aud (audience)
//////        iat (issued at)
//////        exp (expiration time)
////        Map<String, Object> claimSet = new TreeMap<>();
////        claimSet.put(PublicClaims.ISSUER, "for-deploy-apk@deployapk-1602571937321.iam.gserviceaccount.com");
////        claimSet.put("scope", scope);
////        claimSet.put(PublicClaims.AUDIENCE, "https://oauth2.googleapis.com/token");
////        claimSet.put(PublicClaims.ISSUED_AT, new Date(currentTimeMillis));
////        claimSet.put(PublicClaims.EXPIRES_AT, new Date(currentTimeMillis + ACCESS_TOKEN_EXPIRE_TIME_SEC));
////        System.out.println("Show  GSON　claimSet :  " + sGson.toJson(claimSet));
//
////        JsonWebSignature.Header header = new JsonWebSignature.Header();
////        JsonWebSignature.signUsingRsaSha256.
//        //TODO use library
//        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) privatekey);
////        String builder = JWT.create().withHeader(headers)
//////                .withClaim(PublicClaims.ISSUER, serviceAccountEmail)
//////                .withClaim("scope", scope)
//////                .withClaim(PublicClaims.AUDIENCE, audience)
//////                .withClaim(PublicClaims.EXPIRES_AT, currentTimeSeconds+ACCESS_TOKEN_EXPIRE_TIME_SEC)
//////                .withClaim(PublicClaims.ISSUED_AT, currentTimeSeconds)
//        String builder = JWT.create()
//                .withIssuer(serviceAccountEmail)
//                .withAudience(audience)
//                .withClaim("scope", "https://www.googleapis.com/auth/drive")
//                .withIssuedAt(new Date(currentTimeMillis))
//                .withExpiresAt(new Date(currentTimeMillis + ACCESS_TOKEN_EXPIRE_TIME_SEC)).sign(algorithm);
//        return builder;
//
//    }

//    private static PrivateKey getP12FilePrivateKey(InputStream credentialStream) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        char[] keystorepasswordchar = "notasecret".toCharArray();
//        keyStore.load(credentialStream, keystorepasswordchar);
//        return (PrivateKey) keyStore.getKey(keyStore.aliases().nextElement(), keystorepasswordchar);
//    }

//    https://stackoverflow.com/questions/39887303/resumable-upload-in-drive-rest-api-v3/48846988
    //https://stackoverflow.com/questions/46046046/google-drive-rest-api-resumable-upload-returnin-400-bad-request
    //https://github.com/PiyushXCoder/google-drive-ResumableUpload/blob/master/ResumableUpload.java
    //https://medium.com/@sergei.rybalkin/upload-file-to-google-drive-with-kotlin-931cec5252c1
//    private static String requestUploadUrl( Credential credential) throws MalformedURLException, IOException {
//        URL url = new URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable");
//        HttpURLConnection req = (HttpURLConnection) url.openConnection();
//        req.setRequestMethod("POST");
//        req.setDoInput(true);
//        req.setDoOutput(true);
//        req.setRequestProperty("Authorization",credential.tokenType+" " + credential.accessToken);
//        req.setRequestProperty("X-Upload-Content-Type", jsonStructure.getMimeType());
////        req.setRequestProperty("X-Upload-Content-Length", String.valueOf(jsonStructure.getSize()));
//        req.setRequestProperty("X-Upload-Content-Length", "110240000");
//        req.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//        String body = "{ \"name\": \""+jsonStructure.getName()+"\" }";
//        req.setRequestProperty("Content-Length", String.format(Locale.ENGLISH, "%d", body.getBytes().length));
//        OutputStream outputStream = req.getOutputStream();
//        outputStream.write(body.getBytes());
//        outputStream.close();
//        req.connect();
//        String sessionUri = null;
//        if (req.getResponseCode() == HttpURLConnection.HTTP_OK) {
//            sessionUri = req.getHeaderField("location");
//        }
//        return sessionUri;
//    }

//    public int uploadFilePacket( String sessionUri,  java.io.File file, long chunkStart, long uploadBytes) throws MalformedURLException, IOException{
//        URL url1 = new URL(sessionUri);
//        HttpURLConnection req1 = (HttpURLConnection) url1.openConnection();
//        req1.setRequestMethod("PUT");
//        req1.setDoOutput(true);
//        req1.setDoInput(true);
//        req1.setConnectTimeout(10000);
//        req1.setRequestProperty("Content-Type", jsonStructure.getMimeType());
//        req1.setRequestProperty("Content-Length", String.valueOf(uploadBytes));
//        req1.setRequestProperty("Content-Range", "bytes " + chunkStart + "-" + (chunkStart + uploadBytes -1) + "/" + jsonStructure.getSize());
//        OutputStream outstream = req1.getOutputStream();
//        byte[] buffer = new byte[(int) uploadBytes];
//        FileInputStream fileInputStream = new FileInputStream(file);
//        fileInputStream.getChannel().position(chunkStart);
//        if (fileInputStream.read(buffer, 0, (int) uploadBytes) == -1);
//        fileInputStream.close();
//        outstream.write(buffer);
//        outstream.close();
//        req1.connect();
//        return req1.getResponseCode();
//    }

    //    public void uploadFile(Credential credential, java.io.File file) throws IOException, UploadFileException {
//    public void uploadFile(Credential credential, java.io.File file) throws IOException {
//        String sessionUrl = requestUploadUrl(credential);
//
//        for (long i = 1, j = CHUNK_LIMIT; i = jsonStructure.getSize()) {
//            j = jsonStructure.getSize() - i + 1;
//        }
//        int responseCode = uploadFilePacket(sessionUrl, jsonStructure, file, i - 1, j);
////        if(!(responseCode == OK || responseCode == CREATED || responseCode == INCOMPLETE)) throw new UploadFileException(responseCode);
//    }

    public PrivateKey getPrivateKey(String filepath) throws URISyntaxException, IOException {
        URL url = QuickstartDrive.class.getClassLoader().getResource(filepath);
        File f = new File(url.toURI());
        final InputStream in = new FileInputStream(f);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + filepath);
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

    private static BufferedInputStream getInputStream(File f) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(f));
    }

    private static int readFileMetadata(InputStream in, byte[] RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY) throws IOException {
        int length = 0;
        while (length != GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH) {
//            Show  byteArray ? 262144
//            Show  GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH ? 262144
//            Show  tt ? 262144
//            Show  length ? 24943
//            Show  tt ? 237201
//            System.out.println("Show  RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY ? " + RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY.length);
//            System.out.println("Show  off ? " + length);
//            System.out.println("Show  len ? " + (GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH - length));
            byte[] buffer_into_which_the_data_is_read = RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY;
            int start_offset = length;
            int maximum_number_of_bytes_to_read = GoogleDriveApi.RESUMABLE_UPLOAD_PART_MAX_LENGTH - length;
            int readLength = in.read(buffer_into_which_the_data_is_read, start_offset, maximum_number_of_bytes_to_read);
            if (readLength == -1) {
                break;
            }
            length += readLength;
        }
        return length;
    }


    private static Request getResumableInitRequest(String token, String totalsize) {
        RequestBody requestbody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sGson.toJson(mInfos));
        return new Request.Builder()
                .url("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable")
                .method("POST", requestbody)
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("X-Upload-Content-Type", "application/octet-stream")
                .addHeader("X-Upload-Content-Length", totalsize)
                .build();
    }

    private static Request getResumableUploadingRequest(String token, long totalSize, BufferedInputStream inputStream, long lastByteCount) throws IOException {
        final byte[] RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY = new byte[RESUMABLE_UPLOAD_PART_MAX_LENGTH];


        int datatranslength = readFileMetadata(inputStream, RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY);
        RequestBody filebody = RequestBody.create(null, RESUMABLE_UPLOAD_PART_MAX_LENGTH_BYTEARRAY, 0, datatranslength);
        String range = "bytes " + lastByteCount + "-" + (lastByteCount + datatranslength - 1) + "/" + totalSize;
        if ((lastByteCount + datatranslength - 1) > totalSize) {
            range = "bytes " + lastByteCount + "-" + (totalSize - 1) + "/" + totalSize;
        }
        return new Request.Builder()
                .url(mInfos.mFileUploadUrl)
                .method("PUT", filebody)
                .addHeader("Authorization", token)
                .addHeader("Content-Length", String.valueOf(datatranslength))
                .addHeader("Content-Range", range)
                .build();

    }
}




