package com.szi.plantbuddy;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.szi.plantbuddy.mlmodel.FlowerResult;
import com.szi.plantbuddy.ui.MlResultItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class MlResult extends AppCompatActivity {
    ArrayList<String> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ml_result);

        List<FlowerResult> results = (List<FlowerResult>) getIntent().getSerializableExtra("results");

        if (results != null) {
            ListView resultListView = findViewById(R.id.lResults);
            final MlResultItemAdapter adapter = new MlResultItemAdapter(this, R.layout.plant_item_title, results);
            resultListView.setAdapter(adapter);
        }
    }
}