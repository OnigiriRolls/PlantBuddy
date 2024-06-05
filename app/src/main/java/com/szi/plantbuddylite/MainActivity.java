package com.szi.plantbuddylite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.szi.plantbuddylite.exception.FileException;
import com.szi.plantbuddylite.exception.ModelException;
import com.szi.plantbuddylite.mlmodel.FlowerLabel;
import com.szi.plantbuddylite.mlmodel.MobileNetModel;
import com.szi.plantbuddylite.mlmodel.ModelManager;
import com.szi.plantbuddylite.ui.WaitAnimationDialog;
import com.szi.plantbuddylite.util.FileUtil;
import com.szi.plantbuddylite.util.ImageUtils;
import com.szi.plantbuddylite.util.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final FileUtil FILE_UTILS = new FileUtil();
    private static final String LABELS_JSON_PATH = "oxford_labels.json";
    private ModelManager modelManager;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private WaitAnimationDialog dialog;
    private List<String> results;
    private ExecutorService executorService;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new WaitAnimationDialog();
        executorService = Executors.newSingleThreadExecutor();
        modelManager = new ModelManager();
        modelManager.addModelRunner(new MobileNetModel());

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(FILE_UTILS.getCurrentPhotoPath()));
                            this.imagePath = FILE_UTILS.getCurrentPhotoPath();
                            Bitmap rotatedImage = ImageUtils.rotateBitmap(imageBitmap, 90);
                            dialog.showLoadingDialog(this, this::onDialogDismissed);
                            executorService.execute(() -> runModelAndShowResults(rotatedImage));
                            saveImageToGallery(rotatedImage);
                        } catch (IOException e) {
                            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void runModelAndShowResults(Bitmap imageBitmap) {
        List<String> results = null;
        try {
            List<FlowerLabel> labels = JsonReader.readLabelsJson(LABELS_JSON_PATH, this);
            results = modelManager.runModels(this, imageBitmap, labels);
            dialog.stopAnimationWhenDone();
            this.results = results;
        } catch (ModelException | FileException e) {
            dialog.stopAnimationWhenDone();
            dialog.dismissDialog();
            runOnUiThread(() ->
                    Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show()
            );
        }
    }

    private void onDialogDismissed() {
        if (results != null) {
            startActivityWithResults(results, imagePath);
        }
    }

    private void startActivityWithResults(List<String> results, String imagePath) {
        Intent intent = new Intent(this, MlResultActivity.class);
        intent.putStringArrayListExtra("results", new ArrayList<>(results));
        intent.putExtra("imagePath", imagePath);

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
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cameraActivityResultLauncher.launch(cameraIntent);
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Plant_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (uri == null) {
            Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            if (outputStream == null) {
                Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Toast.makeText(this, "Image saved to gallery.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
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
