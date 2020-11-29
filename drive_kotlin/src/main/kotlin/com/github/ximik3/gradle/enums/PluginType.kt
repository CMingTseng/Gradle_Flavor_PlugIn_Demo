package com.github.ximik3.gradle.enums

enum class PluginType(var value: Int) {
    GAM(1), MIMI(2), UNDEFINED(0);

    companion object {
        fun parse(value: Int): PluginType {
            return when (value) {
                1 -> GAM
                2 -> MIMI
                else -> UNDEFINED
            }
        }
    }
}