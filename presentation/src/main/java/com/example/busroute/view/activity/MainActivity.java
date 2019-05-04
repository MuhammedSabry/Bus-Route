package com.example.busroute.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.busroute.BusRouteApplication;
import com.example.busroute.R;
import com.example.busroute.databinding.ActivityMainBinding;
import com.example.busroute.domain.model.Bus;
import com.example.busroute.model.BusListener;
import com.example.busroute.util.InputTextUtils;
import com.example.busroute.util.TextUtil;
import com.example.busroute.util.ValidationUtil;
import com.example.busroute.viewmodel.MainViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements BusListener {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    private void initActivity() {
        initDataBinding();
        initViewModel();
        initViews();
        requestWritingPermissions();
    }

    private void initViewModel() {
        this.viewModel = ViewModelProviders.of(this, BusRouteApplication.getViewModelFactory()).get(MainViewModel.class);
    }

    private void initViews() {
        disableLoadByNumber();
        disableLoadInMaps();
    }

    private void requestWritingPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    private void onPictureTaken(@NonNull Bitmap bitmap) {
        startLoading();
        viewModel.getBus(bitmap, this);
    }

    private void saveImage(@NonNull Bitmap bitmap) {
        File downloadsDirectory = getFileToSave();

        try (FileOutputStream out = new FileOutputStream(downloadsDirectory)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File getFileToSave() {
        String customFolder = "/busroute";
        File externalDirectory = new File(Environment.getExternalStorageDirectory() + customFolder);

        if (!externalDirectory.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getPath() + customFolder);
            wallpaperDirectory.mkdirs();
        }

        return new File(Environment.getExternalStorageDirectory().getPath() + customFolder,
                "image_" + new Date().toString() + ".jpg");
    }

    private void initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainHandler(this);
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

    public void onBusNumberTextChanged(Editable busNumber) {
        if (!ValidationUtil.isValidText(TextUtil.getText(busNumber)))
            disableLoadByNumber();
        else
            enableLoadByNumber();
    }

    public void onLoadByNumberClicked() {
        startLoading();
        hideKeyboard();
        viewModel.getBus(InputTextUtils.getText(binding.busNumber), this);
    }

    public void onLoadFromImageClicked() {
        hideKeyboard();
        CropImage.activity()
                .start(this);
    }

    private void hideKeyboard() {
        InputTextUtils.hideSoftKeyboard(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    onPictureTaken(bitmap);
                } catch (IOException e) {
                    onFail("Failed to retrieve image");
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                onFail(result.getError().getMessage());
        }
    }

    public void onOpenInMapsClicked() {
        String link = viewModel.getLoadedBus().getLink();
        if (ValidationUtil.isValidText(link)) {
            Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(link));
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) == null) {
                onFail("Device doesn't contain google maps or bus link is invalid");
            } else
                startActivity(mapIntent);
        } else
            onFail("Loaded bus doesn't contain maps link");
    }

    private void disableLoadByNumber() {
        binding.loadByNumber.setEnabled(false);
    }

    private void enableLoadByNumber() {
        binding.loadByNumber.setEnabled(true);
    }

    private void disableLoadInMaps() {
        binding.openInMaps.setEnabled(false);
    }

    private void enableLoadInMaps() {
        binding.openInMaps.setEnabled(true);
    }

    private void startLoading() {
        binding.loadingLayout.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        binding.loadingLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResult(Bus result) {
        stopLoading();
        Toasty.success(this, "Bus loaded successfully").show();
        hideKeyboard();

        onBusDataLoaded(result);
    }

    private void onBusDataLoaded(Bus result) {
        binding.loadedBusNumber.setText(String.valueOf(result.getBusNumber()));

        if (result.getZones() == null || result.getZones().length == 0)
            binding.loadedZones.setText("Zones data unavailable!");
        else
            binding.loadedZones.setText(Arrays.toString(result.getZones()));
        if (ValidationUtil.isValidText(result.getLink()))
            enableLoadInMaps();
    }

    @Override
    public void onSuccess(String message) {
    }

    @Override
    public void onFail(String message) {
        stopLoading();
        Toasty.error(this, message).show();
    }

    @Override
    public void onFallBack() {
        stopLoading();
    }
}
