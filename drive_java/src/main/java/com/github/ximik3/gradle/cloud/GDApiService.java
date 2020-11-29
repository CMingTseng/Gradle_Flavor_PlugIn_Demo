package com.github.ximik3.gradle.cloud;

import com.github.ximik3.gradle.cloud.api.data.response.Credential;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GDApiService {

//    Single<String> generateGoogleAuthJWT(InputStream credentialinputstream);

    @FormUrlEncoded
    @POST
    Call<Credential> fetchToken(@Url String url, @Field("grant_type") String jwt_type, @Field("assertion") String jwt);

    //    https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable
    @POST
    @Headers({
            "X-Upload-Content-Type: application/octet-stream",
            "Content-Type: application/json; charset=UTF-8"
    })
    Call<ResponseBody> resumableUpload(@Url String url, @Header("Authorization") String authorization, @Header("X-Upload-Content-Length") String xuploadcontentlength, @Query("uploadType") String uploadTtype);

    //    https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable&upload_id=ABg5-Uz5TXKi9rGFsBZhPRiVqXajmlJaMhfF8GFPaIQSNrruA3dI2sgstwiU9Mk3IzihYIHqDcurvr7NkQl837iyNvP_2iPPsw
    @Multipart
    @PUT
    Call<ResponseBody> keepUpload(@Url String url, @Header("Authorization") String authorization, @Header("Content-Length") String contentlength, @Header("Content-Range") String contentrange, @Query("uploadType") String uploadTtype, @Query("upload_id") String upload_id, @Part("filemetadate") RequestBody filemetadate);

}

