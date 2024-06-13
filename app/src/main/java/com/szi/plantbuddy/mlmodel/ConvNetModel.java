package com.szi.plantbuddy.mlmodel;

import android.content.Context;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

public class ConvNetModel extends BaseModel<com.szi.plantbuddy.ml.Model129conv17Re2Ad1> {
    @Override
    protected com.szi.plantbuddy.ml.Model129conv17Re2Ad1 createModelInstance(Context context, Model.Options options) throws IOException {
        return com.szi.plantbuddy.ml.Model129conv17Re2Ad1.newInstance(context, options);
    }

    @Override
    protected TensorBuffer runInference(com.szi.plantbuddy.ml.Model129conv17Re2Ad1 model, TensorImage image) {
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
        return true;
    }
}
