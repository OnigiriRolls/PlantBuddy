package com.szi.plantbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.szi.plantbuddy.exception.FileException;
import com.szi.plantbuddy.exception.ModelException;
import com.szi.plantbuddy.mlmodel.FlowerModel;
import com.szi.plantbuddy.util.FileUtil;
import com.szi.plantbuddy.util.ImageUtils;
import com.szi.plantbuddy.util.ThemeManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final FlowerModel FLOWER_MODEL = new FlowerModel();
    private static final FileUtil FILE_UTILS = new FileUtil();
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ImageView imageView;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView1);
        titleText = findViewById(R.id.tTitle);

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(FILE_UTILS.getCurrentPhotoPath()));
                            Bitmap rotatedImage = ImageUtils.rotateBitmap(imageBitmap, 90);
                            titleText.setVisibility(View.INVISIBLE);
                            imageView.setImageBitmap(rotatedImage);
                            runModelAndShowResults(rotatedImage);
                        } catch (IOException e) {
                            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void runModelAndShowResults(Bitmap imageBitmap) {
        List<String> results = null;
        try {
            results = FLOWER_MODEL.runModel(this, imageBitmap);
            startActivityWithResults(results);
        } catch (ModelException e) {
            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
        }
    }

    private void startActivityWithResults(List<String> results) {
        Intent intent = new Intent(this, MlResult.class);
        intent.putStringArrayListExtra("results", new ArrayList<>(results));
        startActivity(intent);
    }

    public void onClick(View view) {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } else {
            startCameraSimple();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraSimple();
            } else {
                Toast.makeText(this, R.string.message_camera_permission_denied, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCameraSimple() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = FILE_UTILS.createImageFile(this);
        } catch (IOException | FileException ex) {
            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
            Log.i("debug", "IOException");
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cameraActivityResultLauncher.launch(cameraIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FILE_UTILS.deleteImagesFromInternalStorage(this);
    }
}
