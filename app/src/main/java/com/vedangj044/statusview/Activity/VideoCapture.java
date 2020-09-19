package com.vedangj044.statusview.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.CameraXConfig;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vedangj044.statusview.Activity.UploadActivity;
import com.vedangj044.statusview.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class VideoCapture extends Fragment {

    private ImageView btnRecord;
    private TextView timeRemaining;
//    private SurfaceView surfaceView;
//    private SurfaceHolder surfaceHolder;
//    private Camera camera;
//    private MediaRecorder mr;
//    private boolean isRecording = false;
//
    private final int MAX_DURATION = 30000;
//
//    private String path;

    // start
    private static final int REQUEST_CAMERA_PERMISSION_REQUEST = 0;
    private static final int REQUEST_STORAGE_PERMISSION_REQUEST = 0;
    private TextureView mTextureView;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            setupCamera(width, height);
            connectCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            if(mIsRecording){
                try {
                    createVideoName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startRecord();
                mMediaRecorder.start();
            }
            else{
                startPreview();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };
    private HandlerThread mBackgroundHandlerThread;
    private Handler mBackgroundHandler;
    private String mCameraId;
    private Size mPreviewSize;
    private MediaRecorder mMediaRecorder;
    private Size mVideoSize;
    private int mTotalRotation;
    private boolean mIsRecording = false;
    private CaptureRequest.Builder mCaputeRquestBuilder;

    private File mVideoFolder;
    private String mVideoFile;

    private static SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private static class CompareSizeByArea implements Comparator<Size>{
        @Override
        public int compare(Size o1, Size o2) {
            return Long.signum((long) o1.getWidth()*o1.getHeight()/ (long) o2.getWidth()*o2.getHeight());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_view, container, false);

        btnRecord = view.findViewById(R.id.capture_video);
        timeRemaining = view.findViewById(R.id.duration_time);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsRecording){
                    mIsRecording = false;
                    btnRecord.setImageResource(R.drawable.video_record_foreground);
                    try {
                        mMediaRecorder.stop();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    mMediaRecorder.reset();

                    List<String> selectedImageList = new ArrayList<>();
                    selectedImageList.add(mVideoFile);

                    Intent intent = new Intent(view.getContext(), UploadActivity.class);
                    intent.putStringArrayListExtra("imageList", (ArrayList<String>) selectedImageList);
                    startActivity(intent);
                    getFragmentManager().beginTransaction().remove(VideoCapture.this).commit();
                }
                else{
                    checkWriteStoragePermission();
                    mIsRecording = true;
                    btnRecord.setImageResource(R.drawable.video_record_stop_foreground);

                    new CountDownTimer(MAX_DURATION, 1000){
                            public void onTick(long millisUntilFinished) {
                                timeRemaining.setText(" "+ millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                timeRemaining.setText("done!");

                                try {
                                    mMediaRecorder.stop();
                                    mMediaRecorder.reset();
                                    btnRecord.setImageResource(R.drawable.video_record_stop_foreground);
                                    mIsRecording = true;
                                    Toast.makeText(view.getContext(), "Done!!", Toast.LENGTH_SHORT).show();

                                    List<String> selectedImageList = new ArrayList<>();
                                    selectedImageList.add(mVideoFile);

                                    Intent intent = new Intent(view.getContext(), UploadActivity.class);
                                    intent.putStringArrayListExtra("imageList", (ArrayList<String>) selectedImageList);
                                    startActivity(intent);
                                    getFragmentManager().beginTransaction().remove(VideoCapture.this).commit();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                }
            }
        });



        createVideoFolder();
        mMediaRecorder = new MediaRecorder();


        mTextureView = (TextureView) view.findViewById(R.id.texture_view);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if(mTextureView.isAvailable()){
            setupCamera(mTextureView.getWidth(), mTextureView.getHeight());
            connectCamera();
        }
        else{
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION_REQUEST){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Camera not available", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[1] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Audio not available", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == REQUEST_STORAGE_PERMISSION_REQUEST){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mIsRecording = true;
                btnRecord.setImageResource(R.drawable.video_record_stop_foreground);
                try {
                    createVideoName();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getActivity(), "Cannot record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupCamera(int width, int height){
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try{
            for(String cameraId: cameraManager.getCameraIdList()){
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if(cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                int deviceOrientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
                mTotalRotation = sensorToDeviceRotation(cameraCharacteristics, deviceOrientation);
                boolean swapRotation = mTotalRotation == 90 || mTotalRotation == 270;

                int rotatedWidth = width;
                int rotatedHeight = height;
                if(swapRotation){
                    rotatedHeight = width;
                    rotatedWidth = height;
                }
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeight);
//                mVideoSize = chooseOptimalSize(map.getOutputSizes(MediaRecorder.class), rotatedWidth, rotatedHeight);

                mPreviewSize = new Size(1080, 1920);

                mCameraId = cameraId;
                return;
            }
        }
        catch (CameraAccessException e){
            e.printStackTrace();
        }

    }

    private void connectCamera(){
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mBackgroundHandler);
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(getActivity(), "Video Requires Camera", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION_REQUEST);
                }
            }
            else{
                cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mBackgroundHandler);
            }
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void startRecord(){

        try {
            setupMediaRecorder();
            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            Surface recordSurface = mMediaRecorder.getSurface();
            mCaputeRquestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            mCaputeRquestBuilder.addTarget(previewSurface);
            mCaputeRquestBuilder.addTarget(recordSurface);

            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, recordSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.setRepeatingRequest(mCaputeRquestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            },null);

        } catch (IOException | CameraAccessException e) {
            e.getMessage();
        }
    }

    private void startPreview(){
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface previewSurface = new Surface(surfaceTexture);

        try {
            mCaputeRquestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            mCaputeRquestBuilder.addTarget(previewSurface);
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.setRepeatingRequest(mCaputeRquestBuilder.build(), null, mBackgroundHandler);
                    }
                    catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }, null);
        }
        catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void closeCamera(){
        if(mCameraDevice != null){
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    private void startBackgroundThread(){
        mBackgroundHandlerThread = new HandlerThread("Camera2VideoImage");
        mBackgroundHandlerThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandlerThread.getLooper());
    }

    private void stopBackgroundThread(){
        mBackgroundHandlerThread.quitSafely();
        try {
            mBackgroundHandlerThread.join();
            mBackgroundHandlerThread = null;
            mBackgroundHandler = null;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int sensorToDeviceRotation(CameraCharacteristics cameraCharacteristics, int deviceOrientation){
        int sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        deviceOrientation = ORIENTATIONS.get(deviceOrientation);
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height){
        List<Size> bigEnough = new ArrayList<>();
        for (Size option:choices){
            if(option.getHeight() == option.getWidth()*height/width && option.getWidth() >= width && option.getHeight() >= height){
                bigEnough.add(option);
            }
        }
        if(bigEnough.size() > 0){
            return Collections.min(bigEnough, new CompareSizeByArea());
        }
        else {
            return choices[0];
        }
    }

    private void createVideoFolder(){
        File movieFile = getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        mVideoFolder = new File(movieFile, "videoStatus");

        if(!mVideoFolder.exists()){
            movieFile.mkdir();
        }
    }

    private File createVideoName() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String prepend = "VID_"+timeStamp+"_";
        File videoFile = File.createTempFile(prepend, ".mp4", getContext().getFilesDir());
        mVideoFile = videoFile.getAbsolutePath();
        Log.v("asad", mVideoFile);
        return videoFile;
    }

    private void checkWriteStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                mIsRecording = true;
                btnRecord.setImageResource(R.drawable.video_record_stop_foreground);
                try {
                    createVideoName();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                startRecord();
                mMediaRecorder.start();
            }
            else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(getActivity(), "Permission needed ", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION_REQUEST);
            }
        }
        else{
            mIsRecording = true;
            btnRecord.setImageResource(R.drawable.video_record_stop_foreground);
            try {
                createVideoName();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            startRecord();
            mMediaRecorder.start();
        }
    }

    private void setupMediaRecorder() throws IOException{
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mMediaRecorder.setOutputFile(mVideoFile);
        mMediaRecorder.setVideoFrameRate(30);
//        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());

        mMediaRecorder.setMaxDuration(MAX_DURATION);
        mMediaRecorder.setOrientationHint(mTotalRotation);
        mMediaRecorder.prepare();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}