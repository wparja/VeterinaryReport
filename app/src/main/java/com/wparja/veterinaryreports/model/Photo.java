package com.wparja.veterinaryreports.model;

import android.graphics.Bitmap;

import java.io.File;

public class Photo {

    private Bitmap mBitmap;
    private String mName;
    private String mAbsolutePath;


    public Photo(Bitmap bitmap, File file) {
        mBitmap = bitmap;
        mName = file.getName();
        mAbsolutePath = file.getAbsolutePath();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getName() {
        return mName;
    }

    public String getAbsolutePath() {
        return mAbsolutePath;
    }
}
