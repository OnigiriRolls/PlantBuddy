package com.szi.plantbuddy.mlmodel;

import android.graphics.Bitmap;

import com.szi.plantbuddy.MainActivity;
import com.szi.plantbuddy.exception.ModelException;

import java.util.List;

public interface IModelRunner {
    List<FlowerResult> runModel(MainActivity mainActivity, Bitmap imageBitmap, List<FlowerLabel> labels) throws ModelException;
}
