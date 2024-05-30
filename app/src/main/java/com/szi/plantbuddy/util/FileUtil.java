package com.szi.plantbuddy.util;

import static com.szi.plantbuddy.exception.FileException.DIRECTORY_EXCEPTION_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import com.szi.plantbuddy.exception.FileException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private String currentPhotoPath;

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public File createImageFile(AppCompatActivity activity) throws IOException, FileException {
        String timeStamp = new SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = new File(activity.getFilesDir(), "pictures");

        if (storageDir.exists() || storageDir.mkdirs()) {
            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );

            currentPhotoPath = "file:" + image.getAbsolutePath();
            return image;
        }

        throw new FileException(DIRECTORY_EXCEPTION_MESSAGE);
    }

    public void deleteImagesFromInternalStorage(AppCompatActivity activity) {
        File storageDir = new File(activity.getFilesDir(), "pictures");

        if (storageDir.exists() && storageDir.isDirectory()) {
            File[] files = storageDir.listFiles();

            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

}
