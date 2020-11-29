package com.github.ximik3.gradle;

import com.android.build.gradle.api.ApplicationVariant;
import com.github.ximik3.gradle.cloud.OkHttpFactory;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;

public class DriveUploadTask extends DefaultTask {
    final String APK_MIME_TYPE = "application/vnd.android.package-archive";
    public DrivePluginExtension extension;
    public ApplicationVariant variant;
    public DriveUploader uploader;
    public String flavor;
    public String buildType;
    public Project project;
    public String version;

    @TaskAction
    public void upload() throws FileNotFoundException {
        // get gradle ext properties
        Map<String, Object> properties = getExtraPropertiesExtension(project).getProperties();
        String certificateFilepath = extension.certificateFile;


        File f = new File(certificateFilepath);

        if (!f.exists()) {
            throw new FileNotFoundException("Resource not found: " + certificateFilepath);
        }
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        OkHttpClient.Builder builder = OkHttpFactory.getOkHttpFactory().getOkHttpClientBuilder();
//        if (uploader == null) {
//            AccessToken accessToken = AuthorizationHelper.authorize(extension);
//            println 'Token from googleapis.com retrieved'
//
//            uploader = new DriveUploader(accessToken);
//        }
//        variant.outputs
//                .findAll() { variantOutput -> variantOutput instanceof ApkVariantOutput
//        }
//                .each { variantOutput -> uploader.upload(variantOutput.outputFile, APK_MIME_TYPE, extension.folderId)
//        }
    }

    private final ExtraPropertiesExtension getExtraPropertiesExtension(Project project) {
        return project.getExtensions().getByType(ExtraPropertiesExtension.class);
    }
}
