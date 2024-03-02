package com.szi.plantbuddy;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MlResult extends AppCompatActivity {
    ArrayList<String> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ml_result);

        List<String> results = getIntent().getStringArrayListExtra("results");

        if (results != null) {
            ListView resultListView = findViewById(R.id.lResults);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
            resultListView.setAdapter(adapter);
        }
    }
}