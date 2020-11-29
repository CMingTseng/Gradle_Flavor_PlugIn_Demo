package com.github.ximik3.gradle;

import com.google.common.base.Preconditions;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.api.BaseVariant;
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension;

import org.gradle.api.DomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.mortbay.log.Log;

import java.util.Optional;

public class DrivePlugin implements Plugin<Project> {
    public static final String PLUGIN_EXT_TAG = "deploy";
    public static final String GROUP_OF_TASK = "deployAPK";
    public static final String TASK_TYPE = "Google Drive";
    private final static String TAG = "DrivePlugin";

    @Override
    public void apply(Project project) {
        boolean hasAppPlugin = project.getPlugins().hasPlugin("com.android.internal.application");
        if (!hasAppPlugin) {
            throw new IllegalStateException("The 'com.android.application' plugin is required.");
        }
        DrivePluginExtension extension = project.getExtensions().create(PLUGIN_EXT_TAG, DrivePluginExtension.class);
//        ApplicationExtension android = (ApplicationExtension) getAndroidExtension(project);
        AppExtension appExtension = (AppExtension) getAndroidExtension(project);
        DomainObjectSet<ApplicationVariant> variants = appExtension.getApplicationVariants();
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

        variants.all(variant -> {
            switch (PluginType.parse(extension.type)) {
                case GAM:
                    createGoUploadTask(variant, project, extension);
                    break;
                case UNDEFINED:
                    Log.warn("** upload plugin 設定錯誤: type 不能是 " + extension.type);
                    break;
            }
        });
    }

    private void createGoUploadTask(ApplicationVariant variant, Project project, DrivePluginExtension extension) {
        String fullBuildType = variant.getBuildType().getName();
        String buildType = fullBuildType.split("_")[0];
        String flavorName = variant.getFlavorName();

        if (buildType.contains("dev") || fullBuildType.contains("debug")) return;

        // check needed plugin param
        try {
            if (ApkUpgradeType.parse(extension.apkUpgradeType) == ApkUpgradeType.UNDEFINED)
                throw new Exception("apkUpgradeType 不能是 " + extension.apkUpgradeType);
            if (extension.version.isEmpty())
                throw new Exception("version 必須設定");
        } catch (Exception e) {
            Log.warn("** upload plugin 設定錯誤: " + e.getMessage());
            return;
        }

        String taskName = "uploadApk_" + flavorName + "_" + buildType;
        if (project.getTasksByName(taskName, false).size() > 0) return;
        GoUploadTask task = project.getTasks().create(taskName, GoUploadTask.class);
        task.project = project;
        task.flavor = flavorName;
        task.buildType = buildType;
        task.upgradeType = ApkUpgradeType.parse(extension.apkUpgradeType);
        task.version = extension.version;
        task.setGroup("upload " + flavorName);
        System.out.println("add task: " + taskName);
    }

    private BaseVariant getBaseVariant(Project project) {
        final Logger log = project.getLogger();
//        ApplicationExtension android = (ApplicationExtension) getAndroidExtension(project);
        AppExtension appExtension = (AppExtension) getAndroidExtension(project);
        Optional<ApplicationVariant> optionalBaseVariant =
                appExtension
                        .getApplicationVariants()
                        .stream()
                        .filter(variant -> variant.getName().equals("android"))
                        .findFirst();
        DomainObjectSet<ApplicationVariant> variants = appExtension.getApplicationVariants();
        for (ApplicationVariant variant : variants) {
            Log.debug(TAG + "__" + "Show  variant name : " + variant.getName());
        }
        Preconditions.checkArgument(optionalBaseVariant.isPresent());
        return optionalBaseVariant.get();
    }

    private final BaseExtension getAndroidExtension(Project project) {
        return (BaseExtension) project.getExtensions().getByName("android");
    }

    private final BaseAppModuleExtension getBaseAndroidExtension(Project project) {
        return (BaseAppModuleExtension) project.getExtensions().getByType(BaseAppModuleExtension.class);
    }

    private final ExtraPropertiesExtension getExtraPropertiesExtension(Project project) {
        return (ExtraPropertiesExtension) project.getExtensions().getByType(ExtraPropertiesExtension.class);
    }

    private final String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
