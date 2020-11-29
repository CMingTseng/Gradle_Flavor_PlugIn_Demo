package com.github.ximik3.gradle.cloud;

import com.github.ximik3.gradle.cloud.api.data.response.go.ApiBaseItem;
import com.github.ximik3.gradle.cloud.api.data.response.go.FileInfoItem;
import com.github.ximik3.gradle.cloud.api.data.response.go.UploadFileItem;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface GOApiService {
    @GET("/publicStorage/v1/getinfo")
    Single<ApiBaseItem<FileInfoItem>> getFileInfo(@Query("name") String fileName);

    @Multipart
    @POST("/storage/v1/upload")
    Single<ApiBaseItem<UploadFileItem>> uploadFile(@PartMap Map<String, RequestBody> params);
}
