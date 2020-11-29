package com.github.ximik3.gradle.cloud.api.data.response.go;

import com.google.gson.annotations.SerializedName;

public class ApiBaseItem<T> {

    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
