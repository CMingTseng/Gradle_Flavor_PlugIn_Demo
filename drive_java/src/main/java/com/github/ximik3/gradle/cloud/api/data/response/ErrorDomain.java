package com.github.ximik3.gradle.cloud.api.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorDomain {
    /**
     * errors : [{"domain":"global","reason":"authError","message":"Invalid
     * Credentials","locationType":"header","location":"Authorization"}] code : 401 message :
     * Invalid Credentials
     */

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("errors")
    private List<ErrorDetail> errors;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDetail> errors) {
        this.errors = errors;
    }
}
