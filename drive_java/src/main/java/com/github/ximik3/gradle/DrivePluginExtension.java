package com.github.ximik3.gradle;

import java.util.HashMap;

public class DrivePluginExtension {
    int type = 0;
    /**
     * @see PluginType
     */
    int apkUpgradeType = 0;
    /**
     * @see ApkUpgradeType
     */
    String version = "";
    String account;
    String certificateFile;
    String keyStorePassword;
    HashMap<String, String> folderIds;
}
