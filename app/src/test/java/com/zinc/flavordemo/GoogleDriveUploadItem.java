package com.zinc.flavordemo;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GoogleDriveUploadItem {
    /**
     * name : test.apk file_upload_id : ABg5-UxtICmTETH8VSPLNkUeXm6SdNzRtqN7mG_nTWVky49PGIOxUGM_3BJGXwLrBiJIUmeMXnyb12AtPPAWvvyUAss
     * file_total_size : 81920 current_file_upload_section : 512 file_upload_url :
     * https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable&upload_id=ABg5-UxtICmTETH8VSPLNkUeXm6SdNzRtqN7mG_nTWVky49PGIOxUGM_3BJGXwLrBiJIUmeMXnyb12AtPPAWvvyUAss
     * file_upload_state_code : 308 parents : ["1BLsjBuvKIuXg96hvWwB4EkJ32ZWakAZi"]
     */
    public File file;
    @SerializedName("name")
    public String mFileName;
    @SerializedName("file_upload_id")
    public String mFileUploadId;
    @SerializedName("file_total_size")
    public Long mFileTotalSize;
    @SerializedName("current_file_upload_section")
    public Integer mCurrentFileUploadSection;
    @SerializedName("file_upload_url")
    public String mFileUploadUrl;
    @SerializedName("file_upload_state_code")
    public Integer mFileUploadStateCode;
    @SerializedName("parents")
    private List<String> mParents;
    @SerializedName("id")
    public String file_store_id;
    @SerializedName("mimeType")
    public String file_mimeType;
    @SerializedName("kind")
    public String kind;

    public List<String> getFolderIds() {
        return mParents;
    }

    public void setFolderIds(List<String> folderids) {
        if (mParents == null) {
            mParents = new ArrayList<>();
        }
        if (folderids != null) {
            mParents.addAll(folderids);
        }
    }

    public void addFolderIds(String folderid) {
        if (mParents == null) {
            mParents = new ArrayList<>();
        }
        mParents.add(folderid);
    }
}


//data class GoogleDriveUploadItem(
//        /**
//         * name : test.apk
//         * file_upload_id : ABg5-UxtICmTETH8VSPLNkUeXm6SdNzRtqN7mG_nTWVky49PGIOxUGM_3BJGXwLrBiJIUmeMXnyb12AtPPAWvvyUAss
//         * file_total_size : 81920
//         * current_file_upload_section : 512
//         * file_upload_url : https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable&upload_id=ABg5-UxtICmTETH8VSPLNkUeXm6SdNzRtqN7mG_nTWVky49PGIOxUGM_3BJGXwLrBiJIUmeMXnyb12AtPPAWvvyUAss
//         * file_upload_state_code : 308
//         * parents : ["1BLsjBuvKIuXg96hvWwB4EkJ32ZWakAZi"]
//         */
//        val file: File,
//
//        @SerializedName("name")
//        var mFileName: String? = null,
//
//        @SerializedName("file_upload_id")
//        var mFileUploadId: String? = null,
//
//        @SerializedName("file_total_size")
//        var mFileTotalSize: Long? = null,
//
//        @SerializedName("current_file_upload_section")
//        var mCurrentFileUploadSection: Int? = null,
//
//        @SerializedName("file_upload_url")
//        var mFileUploadUrl: String? = null,
//
//        @SerializedName("file_upload_state_code")
//        var mFileUploadStateCode: Int? = null,
//
//        @SerializedName("parents")
//        private var mParents: MutableList<String?>? = null,
//
//        @SerializedName("id")
//        var file_stroe_id: String? = null,
//
//        @SerializedName("mimeType")
//        var file_mimeType: String? = null,
//
//        @SerializedName("kind")
//        var kind: String? = null,
//        )
//
////FIXME !!!
////fun getFolderIds(): List<String?>? {
////    return mParents
////}
////
////fun setFolderIds(folderids: List<String?>?) {
////    if (mParents == null) {
////        mParents = ArrayList()
////    }
////    if (folderids != null) {
////        mParents!!.addAll(folderids)
////    }
////}
////
////fun addFolderIds(folderid: String?) {
////    if (mParents == null) {
////        mParents = ArrayList()
////    }
////    mParents!!.add(folderid)
////}