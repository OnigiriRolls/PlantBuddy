package com.szi.plantbuddy;

import android.os.Bundle;
import android.util.Rational;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.camera.core.resolutionselector.AspectRatioStrategy;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import androidx.lifecycle.LifecycleOwner;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends BaseActivity {
    private Button takePhotoButton;
    private TextureView textureView;
    private PermissionManager permissionManager;
    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhotoButton = findViewById(R.id.bScan);
        textureView = findViewById(R.id.textureView);
        permissionManager = PermissionManager.getInstance(this);
    }

    public void takePhoto(View view) {
        if (!permissionManager.checkPermissions(permissions)) {
            permissionManager.askPermissions(MainActivity.this, permissions, 100);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100) {
            permissionManager.handlePermissionResult(MainActivity.this, 100, permissions, grantResults);

            openCamera();
        }
    }

    private void openCamera() {
        CameraX.unbindAll();

        Rational rational = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screenSize = new Size(textureView.getWidth(), textureView.getHeight());
        AspectRatio aspectRatio = new AspectRatio(rational);

        ResolutionSelector resolutionSelector = new ResolutionSelector.Builder()
                .setResolutionStrategy(new ResolutionStrategy(screenSize, ResolutionStrategy.FALLBACK_RULE_NONE))
                .build();

        Preview preview = new Preview.Builder()
                .setResolutionSelector(resolutionSelector)
                .build();

//        preview.setOnPreviewOutputUpdateListener(
//                new _Preview.OnPreviewOutputUpdateListenerQ._t
//
//        @Override
//        public void onUpdated(Preview.PreviewOutput output) (
//                ViewGroup parent = (ViewGroup) textureView.getParent();
//        parent.removeVíew(textureView);
//        parent.addView(textureView, index0);
//        textureView.setSurfaceTexture(output.getSurfaceTexture());
//
//        ImageCaptureConfig imageCaptureConfig =
//                new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
//        setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).bu1ld();
//        imgCap = new ImageCapture(imageCaptureConfig);
////bind to lifecycle:
//        Camerax.bindToLifecycle((LifecycleOwner) this, preview, imgCap);
//        private void updateTransform() t
//        Matrix mx = new Matrix();
//        float W textureView. .getHeasuredWidth();
    }

}