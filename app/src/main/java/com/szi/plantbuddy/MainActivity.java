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
import com.szi.plantbuddy.util.WaitAnimationDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final FileUtil FILE_UTILS = new FileUtil();
    private static final String LABELS_JSON_PATH = "oxford_labels.json";
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private WaitAnimationDialog dialog;
    private List<FlowerResult> results;
    private ExecutorService executorService;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new WaitAnimationDialog();
        executorService = Executors.newSingleThreadExecutor();

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(FILE_UTILS.getCurrentPhotoPath()));
                            this.imagePath = FILE_UTILS.getCurrentPhotoPath();
                            Bitmap rotatedImage = ImageUtils.rotateBitmap(imageBitmap, 90);
                            Log.d("debug", "before run");
                            dialog.showLoadingDialog(this, this::onDialogDismissed);
                            executorService.execute(() -> runModelAndShowResults(rotatedImage));
                            Log.d("debug", "after run");
                        } catch (IOException e) {
                            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void runModelAndShowResults(Bitmap imageBitmap) {
        Log.d("debug", "before results");
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
            dialog.stopAnimationWhenDone();
            Log.d("debug", "after hide");
            this.results = results;
        } catch (ModelException | FileException e) {
            dialog.stopAnimationWhenDone();
            Log.d("debug", "after hide");
            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
        }
    }

    private void onDialogDismissed() {
        Log.d("debug", "dialog dismissed");
        if (results != null) {
            startActivityWithResults(results, imagePath);
        }
    }

    private void startActivityWithResults(List<FlowerResult> results, String imagePath) {
        Intent intent = new Intent(this, MlResult.class);
        intent.putExtra("results", (Serializable) results);
        intent.putExtra("imagePath", imagePath);

        startActivity(intent);
    }

    public void onClick(View view) {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } else {
            startCameraSimple();
            Log.d("debug", "after start camera simple");
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
            Log.d("debug", "after launch");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dialog.dismissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FILE_UTILS.deleteImagesFromInternalStorage(this);
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
