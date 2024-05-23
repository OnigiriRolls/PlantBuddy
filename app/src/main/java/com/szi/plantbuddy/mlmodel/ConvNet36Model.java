package com.szi.plantbuddy.mlmodel;

import android.content.Context;

import org.tensorflow.lite.support.common.Operator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvNet36Model extends BaseModel<com.szi.plantbuddy.ml.Model12Above332> {
    @Override
    protected com.szi.plantbuddy.ml.Model12Above332 createModelInstance(Context context, Model.Options options) throws IOException {
        return com.szi.plantbuddy.ml.Model12Above332.newInstance(context, options);
    }

    @Override
    protected TensorBuffer runInference(com.szi.plantbuddy.ml.Model12Above332 model, TensorImage image) {
        return model.process(image.getTensorBuffer()).getOutputFeature0AsTensorBuffer();
    }

    @Override
    protected float getMean() {
        return 0f;
    }

    @Override
    protected float getStddev() {
        return 255f;
    }

    @Override
    protected boolean getApplyCastOp() {
        return false;
    }
}