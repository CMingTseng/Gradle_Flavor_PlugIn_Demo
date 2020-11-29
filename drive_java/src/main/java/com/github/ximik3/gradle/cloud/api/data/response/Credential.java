package com.github.ximik3.gradle.cloud.api.data.response;

import com.google.gson.annotations.SerializedName;

public class Credential {
    /**
     * access_token : wergwergwergwregwerhwrthwrh
     * <p>
     * expires_in : 3599 token_type : Bearer
     * <p>
     * error : unsupported_grant_type error_description : Invalid grant_type:
     */

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("error")
    private String error;
    @SerializedName("error_description")
    private String errorDescription;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
