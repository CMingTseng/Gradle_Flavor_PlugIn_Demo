package com.github.ximik3.gradle.cloud.api.data.response;

import com.google.gson.annotations.SerializedName;

public class ErrorDetail {
    /**
     * domain : global reason : authError message : Invalid Credentials locationType : header
     * location : Authorization
     */

    @SerializedName("domain")
    private String domain;
    @SerializedName("reason")
    private String reason;
    @SerializedName("message")
    private String message;
    @SerializedName("locationType")
    private String locationType;
    @SerializedName("location")
    private String location;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
