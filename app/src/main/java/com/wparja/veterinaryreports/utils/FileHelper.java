package com.wparja.veterinaryreports.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileHelper {

    public static final String BASE_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Veterinary Reports/";
    public static final String PHOTOS = "/photos/";
    public static final String FILES = "/files/";

    public static File geMainFolder(Context context, String folderName) throws Exception {
        String baseFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        File mainFolder = new File(baseFolder + folderName);
        boolean success = true;
        if (!mainFolder.exists()) {
            success = mainFolder.mkdirs();
        }

        if (!success) {
            throw new Exception();
        }

        return mainFolder;
    }

    public static File gePhotoFolder(Context context, String folderName) throws Exception {
        String baseFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        boolean success = true;
        File photosFolder = new File(baseFolder + folderName + PHOTOS);
        if (!photosFolder.exists()) {
            success = photosFolder.mkdirs();
        }

        if (!success) {
            throw new Exception();
        }

        return photosFolder;
    }

    public static File getFilesFolder(Context context, String folderName) throws Exception {
        String baseFolder = context.getExternalFilesDir(null).getAbsolutePath() + "/";
        boolean success = true;
        File filesFolder = new File(baseFolder + folderName + FILES);
        if (!filesFolder.exists()) {
            success = filesFolder.mkdirs();
        }

        if (!success) {
            throw new Exception();
        }

        return filesFolder;
    }
}
