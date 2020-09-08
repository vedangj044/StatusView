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
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.vedangj044.statusview.Adapters.GalleryImageAdapter;
import com.vedangj044.statusview.GalleryViewFragment;
import com.vedangj044.statusview.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

        RecyclerView recyclerImage = findViewById(R.id.recyle_image);
        recyclerImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        GalleryImageAdapter mAdapter = new GalleryImageAdapter(this, 100);
        recyclerImage.setAdapter(mAdapter);

        // todo creating custom view
        // https://stackoverflow.com/questions/47107105/android-button-has-setontouchlistener-called-on-it-but-does-not-override-perform

        mPreviewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                if(action == MotionEvent.ACTION_DOWN){
                    return true;
                }
                if(action == MotionEvent.ACTION_UP){
                    Toast.makeText(CameraActivty.this, "as", Toast.LENGTH_SHORT).show();

                    FrameLayout fragmentLayout = new FrameLayout(CameraActivty.this);
                    // set the layout params to fill the activity
                    fragmentLayout.setLayoutParams(new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                // set an id to the layout
                    fragmentLayout.setId(R.id.fragmentLayout); // some positive integer
// set the layout as Activity content
                    setContentView(fragmentLayout);
                    GalleryViewFragment grp = new GalleryViewFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, grp).commit();

                    return true;
                }

                return false;
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
                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                File File = new File(getBatchDirectoryName(), mDateFormat.format(new Date())+ ".jpg");
                ImageCapture.OutputFileOptions file = new ImageCapture.OutputFileOptions.Builder(File).build();
                imageCapture.takePicture(file, executor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Intent intent = new Intent(CameraActivty.this, UploadActivity.class);
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