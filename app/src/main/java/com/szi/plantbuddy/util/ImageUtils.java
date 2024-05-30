package com.szi.plantbuddy.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.ops.CastOp;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

public class ImageUtils {
    public static Bitmap rotateBitmap(Bitmap source, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static TensorImage processTensorImage(TensorImage img, float mean, float stddev, boolean applyCastOp) {
        ImageProcessor.Builder builder = new ImageProcessor.Builder()
                .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                .add(new NormalizeOp(mean, stddev));

        if (applyCastOp) {
            builder.add(new CastOp(DataType.FLOAT32));
        }

        ImageProcessor imageProcessor = builder.build();
        return imageProcessor.process(img);
    }
}
