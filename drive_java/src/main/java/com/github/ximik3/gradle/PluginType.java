package com.github.ximik3.gradle;

enum PluginType {
    GAM(1),
    MIMI(2),
    UNDEFINED(0);

    int value;

    PluginType(int value) {
        this.value = value;
    }

    public static PluginType parse(int value) {
        switch (value) {
            case 1:
                return GAM;
            case 2:
                return MIMI;
            default:
                return UNDEFINED;
        }
    }
}
