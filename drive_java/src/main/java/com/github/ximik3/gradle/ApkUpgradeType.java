package com.github.ximik3.gradle;

/**
 * 用來設定版本更新形式(依 minVersion) 1: minVersion 不變 2: minVersion 設為上一版 version 3: 強制更新
 **/
enum ApkUpgradeType {
    MIN_VERSION_NOT_CHANGE(1),
    MIN_VERSION_IS_CURRENT_ONLINE_VERSION(2),
    FORCE_UPGRADE(3),
    UNDEFINED(0);

    int value;

    ApkUpgradeType(int value) {
        this.value = value;
    }

    public static ApkUpgradeType parse(int value) {
        switch (value) {
            case 1:
                return MIN_VERSION_NOT_CHANGE;
            case 2:
                return MIN_VERSION_IS_CURRENT_ONLINE_VERSION;
            case 3:
                return FORCE_UPGRADE;
            default:
                return UNDEFINED;
        }
    }
}
