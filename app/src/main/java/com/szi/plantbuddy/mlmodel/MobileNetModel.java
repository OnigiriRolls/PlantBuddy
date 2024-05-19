package com.szi.plantbuddy.mlmodel;

import android.content.Context;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

public class MobileNetModel extends BaseModel<com.szi.plantbuddy.ml.Model93001> {
    @Override
    protected com.szi.plantbuddy.ml.Model93001 createModelInstance(Context context, Model.Options options) throws IOException {
        return com.szi.plantbuddy.ml.Model93001.newInstance(context, options);
    }

    @Override
    protected TensorBuffer runInference(com.szi.plantbuddy.ml.Model93001 model, TensorImage image) {
        return model.process(image.getTensorBuffer()).getOutputFeature0AsTensorBuffer();
    }
}
