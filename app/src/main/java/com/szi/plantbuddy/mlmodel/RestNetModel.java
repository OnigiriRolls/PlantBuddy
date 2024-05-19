package com.szi.plantbuddy.mlmodel;

import android.content.Context;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

public class RestNetModel extends BaseModel<com.szi.plantbuddy.ml.Model13Retrain> {
    @Override
    protected com.szi.plantbuddy.ml.Model13Retrain createModelInstance(Context context, Model.Options options) throws IOException {
        return com.szi.plantbuddy.ml.Model13Retrain.newInstance(context, options);
    }

    @Override
    protected TensorBuffer runInference(com.szi.plantbuddy.ml.Model13Retrain model, TensorImage image) {
        return model.process(image.getTensorBuffer()).getOutputFeature0AsTensorBuffer();
    }
}