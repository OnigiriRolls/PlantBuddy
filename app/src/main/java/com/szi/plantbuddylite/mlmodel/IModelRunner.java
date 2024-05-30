package com.szi.plantbuddylite.mlmodel;

import android.graphics.Bitmap;

import com.szi.plantbuddylite.MainActivity;
import com.szi.plantbuddylite.exception.ModelException;

import java.util.List;

public interface IModelRunner {
    List<FlowerResult> runModel(MainActivity mainActivity, Bitmap imageBitmap, List<FlowerLabel> labels) throws ModelException;
}
