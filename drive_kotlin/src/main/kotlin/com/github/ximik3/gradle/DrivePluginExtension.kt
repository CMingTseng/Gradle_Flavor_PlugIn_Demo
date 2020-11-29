package com.github.ximik3.gradle

import java.io.File

open class DrivePluginExtension {
    /** @see PluginType */
    var type = 0

    /** @see ApkUpgradeType */
    var apkUpgradeType = 0
    var version: String = ""
    var folderId: String? = null
    var serviceAccountEmail: String? = null
    var pk12File: File? = null
    var certificateFile: String? = null
    var keyStorePassword: String? = null
}