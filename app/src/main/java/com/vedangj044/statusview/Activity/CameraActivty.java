package com.vedangj044.statusview.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.vedangj044.statusview.Adapters.GalleryImageAdapter;
import com.vedangj044.statusview.CustomView.PreviewCustom;
import com.vedangj044.statusview.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivty extends AppCompatActivity {

    // permissions
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO"
    };

    private Executor executor = Executors.newSingleThreadExecutor();

    private PreviewCustom mPreviewView;
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

        RecyclerView recyclerImage = findViewById(R.id.recyle_image);

        // Linear Layout set to horizontal
        recyclerImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        GalleryImageAdapter mAdapter = new GalleryImageAdapter(this, 100);

        // Listener for multiple selection Maybe used later
        mAdapter.setOnNotifyImageData(new GalleryImageAdapter.notifyImageDate() {
            @Override
            public void sendSignal(String text) {

            }

            @Override
            public void sendSelectedItem(List<String> s) {

            }
        });
        recyclerImage.setAdapter(mAdapter);

        // Swipe up action on preview view triggers this listener
        // Creates a fragment of GalleryViewFragment class
        mPreviewView.setmTouchListener(new PreviewCustom.touchListener() {
            @Override
            public void receiveSignal(boolean event) {
                try {
                    ProcessCameraProvider.getInstance(CameraActivty.this).get().unbindAll();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                FrameLayout fragmentLayout = new FrameLayout(CameraActivty.this);
                // set the layout params to fill the activity
                fragmentLayout.setLayoutParams(new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                // set an id to the layout
                fragmentLayout.setId(R.id.fragmentLayout); // some positive integer
                // set the layout as Activity content
                setContentView(fragmentLayout);
                GalleryViewFragment grp = new GalleryViewFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, grp).commit();

            }
        });

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

        captureImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                VideoCapture vd = new VideoCapture();
                try {
                    ProcessCameraProvider.getInstance(CameraActivty.this).get().unbindAll();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                FrameLayout fragmentLayout = new FrameLayout(CameraActivty.this);
                // set the layout params to fill the activity
                fragmentLayout.setLayoutParams(new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                // set an id to the layout
                fragmentLayout.setId(R.id.fragmentLayout); // some positive integer
                // set the layout as Activity content
                setContentView(fragmentLayout);
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, vd).commit();
                return true;
            }
        });

    }


    // Reference for this snippet CAMERAX Documentation
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


        // camera selector from available, lensFacing declared global may change later
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();


        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

        ImageCapture.Builder build = new ImageCapture.Builder();

        // vendor specific onyl if HDR is available
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(build);
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)){
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        // Default Rotation
        final ImageCapture imageCapture = build.setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation()).build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

        // Main camera object which is lifecycle aware
        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);

        // Capture Image
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Name of file is set to current date and time
                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

                // File object output [ can be replaced with bytearray later ]
                File File = new File(getBatchDirectoryName(), mDateFormat.format(new Date())+ ".jpg");
                ImageCapture.OutputFileOptions file = new ImageCapture.OutputFileOptions.Builder(File).build();

                // Take picture
                imageCapture.takePicture(file, executor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                        // Starting the preview activity when file is saved
                        Intent intent = new Intent(CameraActivty.this, UploadActivity.class);

                        // Path to the image
                        intent.putExtra("image", File.getAbsolutePath());
                        startActivity(intent);
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    // App folder to save all images captured from camera
    public String getBatchDirectoryName() {

        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {

        }
        return app_folder_path;
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(CameraActivty.this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    // Request for permission or make a toast
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