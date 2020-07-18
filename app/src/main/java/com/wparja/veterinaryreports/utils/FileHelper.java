package com.wparja.veterinaryreports.utils;

import android.os.Environment;
import java.io.File;

public class FileHelper {

    public static final String PHOTOS = "photos/";
    public static final String FILES = "files/";
    public static final String ROOT_FOLDER = "/Veterinary Reports/";

    public static File getRootFolder() throws Exception {
        return createFolder(Environment.getExternalStorageDirectory(), ROOT_FOLDER);
    }

    public static File getFolder(String folderName) throws Exception {
        return createFolder(getRootFolder(), folderName);
    }

    public static File gePhotoFolder(String folderName) throws Exception {
        File mainFolder = createFolder(getRootFolder(), folderName);
        return createFolder(mainFolder, PHOTOS);
    }

    public static File getFilesFolder(String folderName) throws Exception {
        File mainFolder = createFolder(getRootFolder(), folderName);
        return createFolder(mainFolder, FILES);
    }

    private static File createFolder(File root, String folderName) throws Exception {
        File folder = new File(root, folderName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        if (!success) {
            throw new Exception();
        }

        return folder;
    }
}
