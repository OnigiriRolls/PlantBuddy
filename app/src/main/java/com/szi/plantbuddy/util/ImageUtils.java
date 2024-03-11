package com.szi.plantbuddy.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtils {

    public static Bitmap rotateBitmap(Bitmap source, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
