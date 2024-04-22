package com.szi.plantbuddy.util;

import static com.szi.plantbuddy.exception.FileException.JSON_EXCEPTION_MESSAGE;

import com.google.gson.Gson;
import com.szi.plantbuddy.exception.FileException;
import com.szi.plantbuddy.mlmodel.FlowerLabel;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    public List<FlowerLabel> readLabelsJson(String path) throws FileException {
        try (Reader reader = new FileReader(path)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, List.class);
        } catch (Exception e) {
            throw new FileException(JSON_EXCEPTION_MESSAGE);
        }
    }
}
