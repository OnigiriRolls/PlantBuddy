package com.szi.plantbuddylite.mlmodel;

import android.content.Context;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

public class MobileNetModel extends BaseModel<com.szi.plantbuddylite.ml.MobnetMayAfterFinePr1> {
    @Override
    protected com.szi.plantbuddylite.ml.MobnetMayAfterFinePr1 createModelInstance(Context context, Model.Options options) throws IOException {
        return com.szi.plantbuddylite.ml.MobnetMayAfterFinePr1.newInstance(context, options);
    }

    @Override
    protected TensorBuffer runInference(com.szi.plantbuddylite.ml.MobnetMayAfterFinePr1 model, TensorImage image) {
        return model.process(image.getTensorBuffer()).getOutputFeature0AsTensorBuffer();
    }

    @Override
    protected float getMean() {
        return 0f;
    }

    @Override
    protected float getStddev() {
        return 1f;
    }

    @Override
    protected boolean getApplyCastOp() {
        return true;
    }
}
