package com.szi.plantbuddy;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.szi.plantbuddy.mlmodel.FlowerResult;
import com.szi.plantbuddy.ui.MlResultItemAdapter;
import com.szi.plantbuddy.util.ImageUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MlResult extends AppCompatActivity {
    private ImageView flowerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ml_result);

        setFlowerImage();
        setResults();
    }

    private void setFlowerImage() {
        flowerImage = findViewById(R.id.flowerImage);
        String imagePath = getIntent().getStringExtra("imagePath");
        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imagePath));
                Bitmap rotatedImage = ImageUtils.rotateBitmap(imageBitmap, 90);
                flowerImage.setImageBitmap(rotatedImage);
            } catch (IOException e) {
                Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setResults() {
        List<FlowerResult> results = (List<FlowerResult>) getIntent().getSerializableExtra("results");
        TextView bestResultText = findViewById(R.id.tBestResult);
        TextView otherGuesses = findViewById(R.id.tGuesses);
        ListView resultListView = findViewById(R.id.lResults);

        if (results != null && results.size() > 0) {
            FlowerResult bestResult = results.get(0);
            String label = StringUtils.capitalize(bestResult.getFlowerLabel());
            bestResultText.setText(label);

            if (bestResult.getProbability() < 2) {
                List<String> goodResults = results.stream().skip(1).map(FlowerResult::getFlowerLabel).collect(Collectors.toList());
                final MlResultItemAdapter adapter = new MlResultItemAdapter(this, R.layout.plant_item_title, goodResults);
                resultListView.setAdapter(adapter);
                otherGuesses.setVisibility(View.VISIBLE);
                resultListView.setVisibility(View.VISIBLE);
                flowerImage.setVisibility(View.GONE);
            } else {
                otherGuesses.setVisibility(View.GONE);
                resultListView.setVisibility(View.GONE);
                flowerImage.setVisibility(View.VISIBLE);
            }
        }
    }
}