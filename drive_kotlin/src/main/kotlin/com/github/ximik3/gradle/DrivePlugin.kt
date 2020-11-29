package com.github.ximik3.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.github.ximik3.gradle.enums.ApkUpgradeType
import com.github.ximik3.gradle.enums.PluginType
import com.github.ximik3.gradle.task.GoUploadTask
import com.google.common.base.Preconditions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.mortbay.log.Log

class DrivePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val hasAppPlugin = project.plugins.hasPlugin("com.android.internal.application")
        check(hasAppPlugin) { "The 'com.android.application' plugin is required." }
        val extension: DrivePluginExtension =
                project.extensions.create(PLUGIN_EXT_TAG, DrivePluginExtension::class.java)
        val appExtension = getAndroidExtension(project) as AppExtension
        val variants = appExtension.applicationVariants
        variants.all { variant: ApplicationVariant ->
            when (PluginType.parse(extension.type)) {
                PluginType.GAM -> createGamUploadTask(
                        variant,
                        project,
                        extension
                )
                PluginType.MIMI -> createMimiUploadTask(
                        variant,
                        project,
                        extension
                )
                else -> Log.warn("** upload plugin 設定錯誤: type 不能是 " + extension.type)
            }
        }

//        ApplicationExtension android =(ApplicationExtension) getAndroidExtension (project);
//        variants.all { applicationVariant: ApplicationVariant ->
////            System.out.println("Show applicationVariant name info : " + applicationVariant.getName());// get demo1Dev_release not use
////            System.out.println("Show info : " + applicationVariant.getBuildType() + "____" + applicationVariant.getFlavorName());
//            val buildtype = applicationVariant.buildType
//            val debuggable = buildtype.isDebuggable
//            val buildTypeName = buildtype.name
//            //            System.out.println("Show buildTypeName info : " + buildTypeName);
//            if (debuggable || buildtype.name.contains("debug")) {
////                log.debug(TAG + "__" + "Skipping debuggable build type ${variant.buildType.name}.");
//                println("Skipping debuggable build type : $buildTypeName")
//                return@all
//            }
//            val flavorName = applicationVariant.flavorName
//            val hasFlavors = !flavorName.isEmpty()
//            val artifactIdSuffix =
//                if (hasFlavors) capitalizeFirstLetter(flavorName.replace('_', '-')) else ""
//            //            System.out.println("Show artifactIdSuffix info : " + artifactIdSuffix);
//            val flavors =
//                applicationVariant.productFlavors
//            val productFlavorNames: MutableList<String?> =
//                ArrayList()
//            for (flavor in flavors) {
//                productFlavorNames.add(capitalizeFirstLetter(flavor.name))
//            }
//            if (productFlavorNames.isEmpty()) {
//                productFlavorNames.add("")
//            }
//            for (flavor in productFlavorNames) {
////                System.out.println("Show Flavor info : " + flavor);
//                val variationName = flavor + buildTypeName
//                //                System.out.println("Show variationName info : " + variationName);// get Demo1dev_release
//                val uploadApkTaskName = "deploy_" + variationName + "_apk"
//                //        // Create and configure upload task for each variant
//                val publishApkTask: com.github.ximik3.gradle.task.DriveUploadTask =
//                    project.tasks.create<com.github.ximik3.gradle.task.DriveUploadTask>(
//                        uploadApkTaskName,
//                        com.github.ximik3.gradle.task.DriveUploadTask::class.java
//                    ) as com.github.ximik3.gradle.task.DriveUploadTask
//                publishApkTask.extension = extension
//                publishApkTask.variant = applicationVariant
//                publishApkTask.setDescription("Uploads the APK for the $variationName build to Google Drive")
//                publishApkTask.setGroup(GROUP_OF_TASK)
//            }
//        }
        //        variants.all(applicationVariant -> {
////            System.out.println("Show applicationVariant name info : " + applicationVariant.getName());// get demo1Dev_release not use
////            System.out.println("Show info : " + applicationVariant.getBuildType() + "____" + applicationVariant.getFlavorName());
//            BuildType buildtype = applicationVariant.getBuildType();
//            boolean debuggable = buildtype.isDebuggable();
//            final String buildTypeName = buildtype.getName();
////            System.out.println("Show buildTypeName info : " + buildTypeName);
//            if (debuggable || buildtype.getName().contains("debug")) {
////                log.debug(TAG + "__" + "Skipping debuggable build type ${variant.buildType.name}.");
//                System.out.println("Skipping debuggable build type : " + buildTypeName);
//                return;
//            }
//            final String flavorName = applicationVariant.getFlavorName();
//            boolean hasFlavors = !flavorName.isEmpty();
//            final String artifactIdSuffix = hasFlavors ? capitalizeFirstLetter(flavorName.replace('_', '-')) : "";
////            System.out.println("Show artifactIdSuffix info : " + artifactIdSuffix);
//            final List<ProductFlavor> flavors = applicationVariant.getProductFlavors();
//            final List<String> productFlavorNames = new ArrayList<>();
//            for (ProductFlavor flavor : flavors) {
//                System.out.println("Show  get ProductFlavors  do something !!!   ");
////                Map<String, ClassField> fields = flavor.getBuildConfigFields();
////                Set<String> keys = fields.keySet();
////                System.out.println("Show  get ProductFlavor BuildConfigFields   do something !!!   " + keys.size());
////                for (String key : keys) {
////                    System.out.println("Show  ProductFlavor  : " + flavor.getName() + "___BuildConfigField key : " + key + "__with ClassField : " + fields.get(key));
////                }
//            }
//            if (productFlavorNames.isEmpty()) {
//                productFlavorNames.add("");
//            }
//            //FIXME  pass can work
//            Set<String> folderIds = extension.folderIds.keySet();
//            for (String folderId : folderIds) {
////                System.out.println("Show key  : " + folderId);
////                System.out.println("Show value  : " + extension.folderIds.get(folderId));
//            }
//
////FIXME  can not get ProductFlavor ExtraProperties (ext)
////FIXME  now put infos at productFlavors.each !!
//            Map<String, Object> extras = getExtraPropertiesExtension(project).getProperties();
//            Set<String> extrasset = extras.keySet();
//            for (String s : extrasset) {
//                System.out.println("Show ExtraProperties key  : " + s);
//                Object o = extras.get(s);
////                System.out.println("Show ExtraProperties value  Object : " + o.getClass());
//                if (o instanceof HashMap && s.equals("folderIds")) {
//                    LinkedHashMap h = (LinkedHashMap) o;
//                    Set<String> hs = h.keySet();
//                    for (String ss : hs) {
//                        System.out.println("Show ExtraProperties value  Object key   : " + ss);
////                        System.out.println("Show ExtraProperties value  Object : " + h.values());
//                    }
//                }
//            }
//
//            for (String flavor : productFlavorNames) {
////                System.out.println("Show Flavor info : " + flavor);
//                String variationName = flavor + buildTypeName;
////                System.out.println("Show variationName info : " + variationName);// get Demo1dev_release
//                String uploadApkTaskName = "deploy_" + variationName + "_apk";
////            // Create and configure upload task for each variant
//                DriveUploadTask publishApkTask = (DriveUploadTask) project.getTasks().create(uploadApkTaskName, DriveUploadTask.class);
//                publishApkTask.extension = extension;
//                publishApkTask.variant = applicationVariant;
//                publishApkTask.setDescription("Uploads the APK for the " + variationName + " build to Google Drive");
//                publishApkTask.setGroup(GROUP_OF_TASK);
//            }
//        });
    }

    private fun createGamUploadTask(
            variant: ApplicationVariant,
            project: Project,
            extension: DrivePluginExtension
    ) {
        val fullBuildType = variant.buildType.name
        val buildType = fullBuildType.split("_").toTypedArray()[0]
        val flavorName = variant.flavorName
        if (buildType.contains("dev") || fullBuildType.contains("debug") || flavorName == "dev") return

        // check needed plugin param
        try {
            if (ApkUpgradeType.parse(extension.apkUpgradeType) == ApkUpgradeType.UNDEFINED)
                throw Exception("apkUpgradeType 不能是 " + extension.apkUpgradeType)
            if (extension.version.isEmpty())
                throw Exception("version 必須設定")
        } catch (e: Exception) {
            Log.warn("** upload plugin 設定錯誤: " + e.message)
            return
        }
        val taskName = "uploadApk_" + flavorName + "_" + buildType
        if (project.getTasksByName(taskName, false).size > 0) return
        val task: GoUploadTask = project.tasks.create(taskName, GoUploadTask::class.java)
        task.variant = variant
        task.flavor = flavorName
        task.buildType = buildType
        task.upgradeType = ApkUpgradeType.parse(extension.apkUpgradeType)
        task.version = extension.version
        task.group = "upload $flavorName"
        println("add task: $taskName")
    }

    private fun createMimiUploadTask(
            variant: ApplicationVariant,
            project: Project,
            extension: DrivePluginExtension
    ) {

    }

    private fun getBaseVariant(project: Project): BaseVariant {
        val log = project.logger
        //        ApplicationExtension android = (ApplicationExtension) getAndroidExtension(project);
        val appExtension = getAndroidExtension(project) as AppExtension
        val optionalBaseVariant = appExtension
                .applicationVariants
                .stream()
                .filter { variant: ApplicationVariant -> variant.name == "android" }
                .findFirst()
        val variants = appExtension.applicationVariants
        for (variant in variants) {
            Log.debug(TAG + "__" + "Show  variant name : " + variant.name)
        }
        Preconditions.checkArgument(optionalBaseVariant.isPresent)
        return optionalBaseVariant.get()
    }

    private fun getAndroidExtension(project: Project): BaseExtension {
        return project.extensions.getByName("android") as BaseExtension
    }

    private fun getBaseAndroidExtension(project: Project): BaseAppModuleExtension {
        return project.extensions.getByType(BaseAppModuleExtension::class.java) as BaseAppModuleExtension
    }

    private fun capitalizeFirstLetter(original: String?): String? {
        return if (original == null || original.length == 0) {
            original
        } else original.substring(0, 1).toUpperCase() + original.substring(1)
    }

    companion object {
        const val PLUGIN_EXT_TAG = "deploy"
        const val GROUP_OF_TASK = "deployAPK"
        const val TASK_TYPE = "Google Drive"
        private const val TAG = "DrivePlugin"
    }
}