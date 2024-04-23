package com.szi.plantbuddy.util;

import static com.szi.plantbuddy.exception.FileException.JSON_EXCEPTION_MESSAGE;

import android.app.Activity;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.szi.plantbuddy.exception.FileException;
import com.szi.plantbuddy.mlmodel.FlowerLabel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonReader {
    public static List<FlowerLabel> readLabelsJson(String path, Activity activity) throws FileException {
        try {
            AssetManager assetManager = activity.getAssets();
            InputStream inputStream = assetManager.open("oxford_labels.json");
            Reader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            FlowerLabel[] flowerArray = gson.fromJson(reader, FlowerLabel[].class);
            inputStream.close();
            reader.close();
            return new ArrayList<>(Arrays.asList(flowerArray));
        } catch (Exception e) {
            throw new FileException(JSON_EXCEPTION_MESSAGE);
        }
    }
}
