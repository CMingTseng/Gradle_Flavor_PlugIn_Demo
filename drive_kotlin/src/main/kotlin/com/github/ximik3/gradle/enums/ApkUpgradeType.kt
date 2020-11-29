package com.github.ximik3.gradle.enums

/**
 * 用來設定版本更新形式(依 minVersion)
 * 1: 不強更, minVersion 不變
 * 2: 強更至上一版, minVersion 設為上一版 version
 * 3: 強更至最新版
 */
enum class ApkUpgradeType(var value: Int) {
    NOT_FORCE_UPGRADE(1),
    FORCE_UPGRADE_TO_LAST_VERSION(2),
    FORCE_UPGRADE_TO_NEW_VERSION(3),
    UNDEFINED(0);

    companion object {
        fun parse(value: Int): ApkUpgradeType {
            return when (value) {
                1 -> NOT_FORCE_UPGRADE
                2 -> FORCE_UPGRADE_TO_LAST_VERSION
                3 -> FORCE_UPGRADE_TO_NEW_VERSION
                else -> UNDEFINED
            }
        }
    }
}