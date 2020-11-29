package com.github.ximik3.gradle.cloud.api.data.response;

import com.google.gson.annotations.SerializedName;

public class UploadResult extends Error {

    /**
     * GD file upload result kind : drive#file id : 1N3zxX8j9V0UMicZOynY-ulNBCJYJMqhP name :
     * Untitled mimeType : multipart/form-data
     */

    @SerializedName("kind")
    private String kind;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("mimeType")
    private String mimeType;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
