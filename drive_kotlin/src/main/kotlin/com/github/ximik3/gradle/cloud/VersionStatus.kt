package com.github.ximik3.gradle.cloud

enum class VersionStatus(val value: Int) {
    DEFAULT(-1),
    UNCHANGED(0),
    UPDATE(1),
    FORCE_UPDATE(2)
}