package com.szi.plantbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.szi.plantbuddy.mlmodels.FlowerModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final FlowerModel FLOWER_MODEL = new FlowerModel();
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ImageView imageView;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView1);
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(currentPhotoPath));
                            imageView.setImageBitmap(imageBitmap);
                            startActivityWithResults(imageBitmap);
                        } catch (IOException e) {
                            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void startActivityWithResults(Bitmap imageBitmap) {
        List<String> results = getResultsFromModel(imageBitmap);

        if (results != null) {
            Intent intent = new Intent(this, MlResult.class);
            intent.putStringArrayListExtra("results", new ArrayList<>(results));
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
        }
    }

    private List<String> getResultsFromModel(Bitmap imageBitmap) {
        FLOWER_MODEL.initModel(this);
        return FLOWER_MODEL.analyze(this, imageBitmap);
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
            photoFile = createImageFile();
        } catch (IOException ex) {
            Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
            Log.i("debug", "IOException");
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cameraActivityResultLauncher.launch(cameraIntent);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}
