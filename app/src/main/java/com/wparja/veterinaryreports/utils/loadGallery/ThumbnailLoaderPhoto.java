package com.wparja.veterinaryreports.utils.loadGallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;

import com.wparja.veterinaryreports.fragments.PhotoGalleryFragment;
import com.wparja.veterinaryreports.model.Photo;
import com.wparja.veterinaryreports.utils.FileHelper;
import com.wparja.veterinaryreports.utils.PictureUtils;

import java.io.File;

public class ThumbnailLoaderPhoto extends HandlerThread {

    private static final String TAG = "ThumbnailLoaderPhoto";
    private boolean mHasQuit = false;
    private Handler mResponseHandler;
    private static final int PHOTO_LOADED = 0;

    public ThumbnailLoaderPhoto(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public void queueThumbnail(String path) {
        try {
            File files = FileHelper.gePhotoFolder(path);
            for (File file : files.listFiles()) {
                Bitmap b1 =  PictureUtils.getScaledBitmap(file.getAbsolutePath(), 100, 100);
                Photo photo = new Photo(b1, file.getName());
                mResponseHandler.obtainMessage(PHOTO_LOADED, photo).sendToTarget();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
