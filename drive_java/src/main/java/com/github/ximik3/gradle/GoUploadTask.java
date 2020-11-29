package com.github.ximik3.gradle;

import com.github.ximik3.gradle.cloud.GOApiService;

import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoUploadTask extends DefaultTask {
    public String flavor;
    public String buildType;
    public Project project;
    public ApkUpgradeType upgradeType;
    public String version;

    @TaskAction
    public void upload() {
        System.out.println("Start [uploadApk_" + flavor + "_" + buildType + "]\nupgradeType:" + upgradeType + "\nversion:" + version);

        // get gradle ext properties
        Map<String, Object> properties = getExtraPropertiesExtension(project).getProperties();

        // 1. get uploadHosts from gradle ext
        // 2. calculate domain
        String uploadHost = ((HashMap<String, String>) properties.get("uploadHosts")).get(flavor + buildType);
        String domain = uploadHost.substring(uploadHost.indexOf("://") + 3).replaceAll("/", "");
        System.out.println("domain: " + domain);

        // 1. get original apk path
        // 2. calculate apk name & path for upload
        String originalApkPath = ((HashMap<String, String>) properties.get("apkPaths")).get(flavor + buildType);
        String apkPath;
        if (originalApkPath.contains("GAM-plugin")) {
            apkPath = originalApkPath.replaceAll("-v\\S*\\.apk", "").concat(".apk");
        } else {
            apkPath = originalApkPath.replaceAll("_v\\S*\\.apk", "").concat(".apk");
        }
        String apkName = apkPath.substring(apkPath.lastIndexOf("/") + 1);
        System.out.println("apkName: " + apkName);
        System.out.println("apkPath: " + apkPath);

        // copy apk for upload
        try {
            FileUtils.copyFile(new File(originalApkPath), new File(apkPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get current online apk file info
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + domain + "/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        GOApiService service = retrofit.create(GOApiService.class);

        CompositeDisposable compositeDisposable = new CompositeDisposable();
//        service.getFileInfo(apkName)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
    }

    private final ExtraPropertiesExtension getExtraPropertiesExtension(Project project) {
        return project.getExtensions().getByType(ExtraPropertiesExtension.class);
    }
}
