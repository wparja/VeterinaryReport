package com.wparja.veterinaryreports.utils.loadGallery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class LoadGalleryService extends IntentService {

    private static final String TAG = "LoadGalleryService";

    public static Intent newIntent(Context context) {
        return new Intent(context, LoadGalleryService.class);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LoadGalleryService(String name) {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
