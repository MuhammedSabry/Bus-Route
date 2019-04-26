package com.example.busroute;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.busroute.databinding.ActivityMainBinding;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    private void initActivity() {
        initDataBinding();
        initViews();
        requestWritingPermissions();
    }

    private void requestWritingPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    private void initViews() {
        binding.camera.setLifecycleOwner(this);
        binding.camera.addCameraListener(new CameraListener() {
            @Override
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
            }

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                MainActivity.this.onPictureTaken(result);
            }
        });
        binding.takePictureButton.setOnClickListener(v -> binding.camera.takePicture());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void onPictureTaken(@NonNull PictureResult result) {
        Log.d("Picture taken", result.toString());

        String customFolder = "/busroute";
        File externalDirectory = new File(Environment.getExternalStorageDirectory() + customFolder);

        if (!externalDirectory.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getPath() + customFolder);
            wallpaperDirectory.mkdirs();
        }

        File downloadsDirectory = new File(Environment.getExternalStorageDirectory().getPath() + customFolder,
                "image_" + new Date().toString() + ".jpeg");
        result.toBitmap(bitmap -> {
            binding.cameraPreview.setImageBitmap(bitmap);

            try (FileOutputStream out = new FileOutputStream(downloadsDirectory)) {

                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1) {
            if (!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toasty.error(this, "Permissions to save images isn't granted").show();
            }
        }
    }
}
