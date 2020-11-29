package com.github.ximik3.gradle.cloud;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpFactory {
    private static OkHttpFactory sFactory = null;
    private static OkHttpClient.Builder sClientBuilder;

    private OkHttpFactory() {

    }

    public static OkHttpFactory getOkHttpFactory() {
        if (sFactory == null) {
            sFactory = new OkHttpFactory();
        }
        return sFactory;
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        if (sClientBuilder == null) {
            sClientBuilder = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return sClientBuilder;
    }
}
