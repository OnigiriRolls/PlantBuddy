package com.szi.plantbuddy.mlmodel;

import android.content.Context;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

public class EfficientNetModel extends BaseModel<com.szi.plantbuddy.ml.ModelEffnet2Apr> {
    @Override
    protected com.szi.plantbuddy.ml.ModelEffnet2Apr createModelInstance(Context context, Model.Options options) throws IOException {
        return com.szi.plantbuddy.ml.ModelEffnet2Apr.newInstance(context, options);
    }

    @Override
    protected TensorBuffer runInference(com.szi.plantbuddy.ml.ModelEffnet2Apr model, TensorImage image) {
        return model.process(image.getTensorBuffer()).getOutputFeature0AsTensorBuffer();
    }
}

