package com.github.ximik3.gradle.cloud;

public enum DeployServerType {
    UNDEFINED(0),
    GO_TYPE_GAM(1),
    CSHARP_TYPE_MIMI(2),
    GOOGLE_DRIVE_TYPE(3),
    GOOGLE_PLAY_TYPE(4);


    int value;

    DeployServerType(int value) {
        this.value = value;
    }

    public static DeployServerType parse(int value) {
        switch (value) {
            case 4:
                return GOOGLE_PLAY_TYPE;
            case 3:
                return GOOGLE_DRIVE_TYPE;
            case 1:
                return GO_TYPE_GAM;
            case 2:
                return CSHARP_TYPE_MIMI;
            default:
                return UNDEFINED;
        }
    }
}
