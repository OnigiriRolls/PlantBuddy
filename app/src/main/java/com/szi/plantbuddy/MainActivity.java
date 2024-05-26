package com.szi.plantbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.szi.plantbuddy.mlmodel.ConvNet36Model;
import com.szi.plantbuddy.mlmodel.ConvNetModel;
import com.szi.plantbuddy.mlmodel.EfficientNetModel;
import com.szi.plantbuddy.mlmodel.FlowerLabel;
import com.szi.plantbuddy.mlmodel.FlowerResult;
import com.szi.plantbuddy.mlmodel.MobileNetModel;
import com.szi.plantbuddy.mlmodel.ModelManager;
import com.szi.plantbuddy.mlmodel.RestNetModel;
import com.szi.plantbuddy.util.FileUtil;
import com.szi.plantbuddy.util.ImageUtils;
import com.szi.plantbuddy.util.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final FileUtil FILE_UTILS = new FileUtil();
    private static final String LABELS_JSON_PATH = "oxford_labels.json";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ImageView imageView;
    private TextView titleText;
    private boolean shouldContinueAnimation = true;
    private AnimatedVectorDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        titleText = findViewById(R.id.tTitle);
        ImageView flowerAnim = findViewById(R.id.flowerAnim);

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(FILE_UTILS.getCurrentPhotoPath()));
                            Bitmap rotatedImage = ImageUtils.rotateBitmap(imageBitmap, 90);
                            titleText.setVisibility(View.GONE);
                            imageView.setImageBitmap(rotatedImage);
                            imageView.setVisibility(View.VISIBLE);
                            runModelAndShowResults(rotatedImage);
                        } catch (IOException e) {
                            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        anim = (AnimatedVectorDrawable) flowerAnim.getDrawable();

        Animatable2.AnimationCallback animationCallback = new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                if (shouldContinueAnimation) {
                    handler.postDelayed(() -> anim.start(), 500);
                }
            }
        };
        anim.registerAnimationCallback(animationCallback);

    }

    @Override
    protected void onStart() {
        super.onStart();
        anim.start();
    }

    private void runModelAndShowResults(Bitmap imageBitmap) {
        List<FlowerResult> results = null;
        try {
            List<FlowerLabel> labels = JsonReader.readLabelsJson(LABELS_JSON_PATH, this);
            ModelManager modelManager = new ModelManager();
            modelManager.addModelRunner(new MobileNetModel());
            modelManager.addModelRunner(new EfficientNetModel());
            modelManager.addModelRunner(new RestNetModel());
            modelManager.addModelRunner(new ConvNetModel());
            modelManager.addModelRunner(new ConvNet36Model());
            results = modelManager.runModels(this, imageBitmap, labels);
            startActivityWithResults(results);
        } catch (ModelException | FileException e) {
            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
        }
    }

    private void startActivityWithResults(List<FlowerResult> results) {
        Intent intent = new Intent(this, MlResult.class);
        intent.putExtra("results", (Serializable) results);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
        shouldContinueAnimation = false;
        handler.removeCallbacksAndMessages(null);
        anim.clearAnimationCallbacks();
    }
}
