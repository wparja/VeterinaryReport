package com.wparja.veterinaryreports.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class PhotoUtils {

    public static File getPhoto(Context context, String path) {
        File filesDir = context.getFilesDir();
        if (filesDir == null) {
            return null;
        }
        return new File(filesDir, path);
    }
}
