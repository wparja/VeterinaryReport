package com.wparja.veterinaryreports.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;


import java.io.File;
import java.io.FileOutputStream;

public class PhotoUtils {

    public static File getPhoto(Context context, String path) {
        File filesDir = context.getFilesDir();
        if (filesDir == null) {
            return null;
        }
        return new File(filesDir, path);
    }


    public static void rotateIfNeeded(String photoPath) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_UNDEFINED:
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 90;
            }

            rotateImage(degree, photoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void rotateImage(int degree, String photoPath) {
        if (degree > 0) {
            try {
                Bitmap b = BitmapFactory.decodeFile(photoPath);
                Matrix matrix = new Matrix();
                if (b.getWidth() > b.getHeight()) {
                    matrix.setRotate(degree);
                    b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                }

                FileOutputStream fOut = new FileOutputStream(photoPath);
                String imageName = photoPath.substring(photoPath.lastIndexOf("/") + 1);
                String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

                FileOutputStream out = new FileOutputStream((photoPath));
                if (imageType.equalsIgnoreCase("png")) {
                    b.compress(Bitmap.CompressFormat.PNG, 100, out);
                } else if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg")) {
                    b.compress(Bitmap.CompressFormat.JPEG, 100, out);
                }

                fOut.flush();
                fOut.close();
                b.recycle();
            } catch (Exception ex) {

            }
        }
    }

}
