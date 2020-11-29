package com.github.ximik3.gradle.cloud.api.data.response;

import com.google.gson.annotations.SerializedName;

public class Error {
    /**
     * error : {"errors":[{"domain":"global","reason":"authError","message":"Invalid
     * Credentials","locationType":"header","location":"Authorization"}],"code":401,"message":"Invalid
     * Credentials"}
     */

    @SerializedName("error")
    private ErrorDomain error;

    public ErrorDomain getError() {
        return error;
    }

    public void setError(ErrorDomain error) {
        this.error = error;
    }
}
