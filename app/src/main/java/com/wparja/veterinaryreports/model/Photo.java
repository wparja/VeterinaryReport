package com.wparja.veterinaryreports.model;

import android.graphics.Bitmap;

public class Photo {

    private Bitmap mBitmap;
    private String mName;

    public Photo(Bitmap bitmap, String name) {
        mBitmap = bitmap;
        mName = name;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getName() {
        return mName;
    }
}
