package com.wparja.veterinaryreports.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.wparja.veterinaryreports.BuildConfig;

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
