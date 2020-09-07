package com.vedangj044.statusview.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.vedangj044.statusview.R;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivty extends AppCompatActivity {

    // permissions
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    private Executor executor = Executors.newSingleThreadExecutor();

    private PreviewView mPreviewView;
    private ImageView captureImage;
    private ImageView lensSwitch;
    private ImageView torchToggle;

    // state of lens facing and torch
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private boolean torch = false;

    // camera object
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_activty);

        mPreviewView = findViewById(R.id.camera);
        captureImage = findViewById(R.id.captureImg);


        // asking for permission
        if(allPermissionsGranted()){
            startCamera();
        }
        else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        // Switch to toggle camera
        lensSwitch = findViewById(R.id.camera_switch);
        lensSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lensFacing == CameraSelector.LENS_FACING_BACK){
                    lensFacing = CameraSelector.LENS_FACING_FRONT;
                }
                else{
                    lensFacing = CameraSelector.LENS_FACING_BACK;
                }
                startCamera();
            }
        });

        // switch to toggle torch
        torchToggle = findViewById(R.id.camera_flash);
        torchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                torch = !torch;
                camera.getCameraControl().enableTorch(torch);
            }
        });
    }

    private void startCamera() {
        // Lists all available camera
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                    // connects the incoming feed to the preview
                    bindPreview(cameraProvider);
                }catch (ExecutionException | InterruptedException e){
                    Log.e("Error", e.toString());
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull  ProcessCameraProvider cameraProvider) {

        // We need to unbind existing camera processes. example while switching the lens
        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();


        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

        ImageCapture.Builder build = new ImageCapture.Builder();

        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(build);
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)){
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        final ImageCapture imageCapture = build.setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation()).build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // functionality required
            }
        });

    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(CameraActivty.this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}