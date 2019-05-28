package com.dreampany.vision.ml;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import androidx.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.google.android.gms.common.images.Size;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 9/28/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CameraSource {
    @SuppressLint("InlinedApi")
    public static final int CAMERA_FACING_BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
    @SuppressLint("InlinedApi")
    public static final int CAMERA_FACING_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private static final int DUMMY_TEXTURE_NAME = 100;
    private static final float ASPECT_RATIO_TOLERANCE = 0.01f;
    protected Activity activity;
    private Camera camera;
    protected int facing = CAMERA_FACING_BACK;
    private int rotation;

    private Size previewSize;
    private final float requestedFps = 20.0f;
    private final int requestedPreviewWidth = 1280;
    private final int requestedPreviewHeight = 960;
    private final boolean requestedAutoFocus = true;

    private SurfaceTexture texture;
    private final GraphicOverlay overlay;
    private boolean usingSurfaceTexture;

    private Thread processingThread;
    private final FrameProcessingRunnable processingRunnable;
    private final Object processorLock = new Object();
    private VisionImageProcessor frameProcessor;

    private final Map<byte[], ByteBuffer> bytesToByteBuffer = new IdentityHashMap<>();

    public CameraSource(Activity activity, GraphicOverlay overlay) {
        this.activity = activity;
        this.overlay = overlay;
        this.overlay.clear();
        processingRunnable = new FrameProcessingRunnable();
    }

    public void release() {
        synchronized (processorLock) {
            stop();
            processingRunnable.release();
            cleanScreen();

            if (frameProcessor != null) {
                frameProcessor.stop();
            }
        }
    }

    public synchronized void stop() {
        processingRunnable.setActive(false);
        if (processingThread != null) {
            try {
                processingThread.join();
            } catch (InterruptedException e) {
                Timber.d("Frame processing thread interrupted on release.");
            }
            processingThread = null;
        }

        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallbackWithBuffer(null);
            try {
                if (usingSurfaceTexture) {
                    camera.setPreviewTexture(null);
                } else {
                    camera.setPreviewDisplay(null);
                }
            } catch (Exception e) {
                Timber.e(e, "Failed to clear camera preview.");
            }
            camera.release();
            camera = null;
        }
        bytesToByteBuffer.clear();
    }

    synchronized public CameraSource start() throws IOException {
        if (camera != null) {
            return this;
        }

        camera = createCamera();
        texture = new SurfaceTexture(DUMMY_TEXTURE_NAME);
        camera.setPreviewTexture(texture);
        usingSurfaceTexture = true;
        camera.startPreview();

        processingThread = new Thread(processingRunnable);
        processingRunnable.setActive(true);
        processingThread.start();
        return this;
    }

    synchronized public CameraSource start(SurfaceHolder surfaceHolder) throws IOException {
        if (camera != null) {
            return this;
        }

        camera = createCamera();
        camera.setPreviewDisplay(surfaceHolder);
        camera.startPreview();

        processingThread = new Thread(processingRunnable);
        processingRunnable.setActive(true);
        processingThread.start();

        usingSurfaceTexture = false;
        return this;
    }

    synchronized public void setFacing(int facing) {
        if ((facing != CAMERA_FACING_BACK) && (facing != CAMERA_FACING_FRONT)) {
            throw new IllegalArgumentException("Invalid camera: " + facing);
        }
        this.facing = facing;
    }

    public Size getPreviewSize() {
        return previewSize;
    }

    public int getCameraFacing() {
        return facing;
    }


    private void cleanScreen() {
        overlay.clear();
    }

    private Camera createCamera() throws IOException {
        int cameraId = getCameraIdOfRequestedCamera(facing);
        if (cameraId == -1) {
            throw new IOException("Could not find requested camera.");
        }
        Camera camera = Camera.open(cameraId);

        SizePair size = getBestPreviewSize(camera, requestedPreviewWidth, requestedPreviewHeight);
        if (size == null) {
            throw new IOException("Could not find suitable preview size.");
        }

        Size pictureSize = size.pictureSize();
        previewSize = size.previewSize();

        int[] previewFpsRange = getBestPreviewFpsRange(camera, requestedFps);
        if (previewFpsRange == null) {
            throw new IOException("Could not find suitable preview frames per second range.");
        }

        Camera.Parameters params = camera.getParameters();
        if (pictureSize != null) {
            params.setPictureSize(pictureSize.getWidth(), pictureSize.getHeight());
        }
        params.setPreviewSize(previewSize.getWidth(), previewSize.getHeight());
        params.setPreviewFpsRange(
                previewFpsRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX],
                previewFpsRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
        params.setPreviewFormat(ImageFormat.NV21);

        setRotation(camera, params, cameraId);

        if (requestedAutoFocus) {
            if (params
                    .getSupportedFocusModes()
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else {
                Timber.i("Camera auto focus is not supported on this device.");
            }
        }

        camera.setParameters(params);

        camera.setPreviewCallbackWithBuffer(new CameraPreviewCallback());
        camera.addCallbackBuffer(createPreviewBuffer(previewSize));
        camera.addCallbackBuffer(createPreviewBuffer(previewSize));
        camera.addCallbackBuffer(createPreviewBuffer(previewSize));
        camera.addCallbackBuffer(createPreviewBuffer(previewSize));

        return camera;
    }

    private void setRotation(Camera camera, Camera.Parameters parameters, int cameraId) {
        WindowManager window = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int degrees = 0;
        int rotation = window.getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                Timber.e("Bad rotation value: " + rotation);
        }

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);

        int angle;
        int displayAngle;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            angle = (cameraInfo.orientation + degrees) % 360;
            displayAngle = (360 - angle) % 360; // compensate for it being mirrored
        } else { // back-facing
            angle = (cameraInfo.orientation - degrees + 360) % 360;
            displayAngle = angle;
        }

        this.rotation = angle / 90;

        camera.setDisplayOrientation(displayAngle);
        parameters.setRotation(angle);
    }

    private static SizePair getBestPreviewSize(Camera camera, int desiredWidth, int desiredHeight) {
        List<SizePair> previewSizes = getPreviewSizes(camera);

        SizePair result = null;
        int minDiff = Integer.MAX_VALUE;
        for (SizePair pair : previewSizes) {
            Size size = pair.previewSize();
            int diff = Math.abs(size.getWidth() - desiredWidth) + Math.abs(size.getHeight() - desiredHeight);
            if (diff < minDiff) {
                result = pair;
                minDiff = diff;
            }
        }
        return result;
    }

    private static int[] getBestPreviewFpsRange(Camera camera, float desiredPreviewFps) {
        List<int[]> previewFpsRanges = camera.getParameters().getSupportedPreviewFpsRange();

        int desiredPreviewFpsScaled = (int) (desiredPreviewFps * 1000.0f);
        int[] result = null;
        int minDiff = Integer.MAX_VALUE;
        for (int[] range : previewFpsRanges) {
            int deltaMin = desiredPreviewFpsScaled - range[Camera.Parameters.PREVIEW_FPS_MIN_INDEX];
            int deltaMax = desiredPreviewFpsScaled - range[Camera.Parameters.PREVIEW_FPS_MAX_INDEX];
            int diff = Math.abs(deltaMin) + Math.abs(deltaMax);
            if (diff < minDiff) {
                result = range;
                minDiff = diff;
            }
        }
        return result;
    }

    private static List<SizePair> getPreviewSizes(Camera camera) {
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> supportedPreviewSizes = params.getSupportedPreviewSizes();
        List<Camera.Size> supportedPictureSizes = params.getSupportedPictureSizes();
        List<SizePair> previewSizes = new ArrayList<>();

        for (Camera.Size previewSize : supportedPictureSizes) {
            float previewAspectRatio = (float) previewSize.width / (float) previewSize.height;
            for (Camera.Size pictureSize : supportedPictureSizes) {
                float pictureAspectRatio = (float) pictureSize.width / (float) pictureSize.height;
                if (Math.abs(previewAspectRatio - pictureAspectRatio) < ASPECT_RATIO_TOLERANCE) {
                    previewSizes.add(new SizePair(previewSize, pictureSize));
                    break;
                }
            }
        }

        if (previewSizes.isEmpty()) {
            Timber.w("No preview sizes have a corresponding same-aspect-ratio picture size");
            for (Camera.Size previewSize : supportedPreviewSizes) {
                previewSizes.add(new SizePair(previewSize, null));
            }
        }

        return previewSizes;
    }

    private static int getCameraIdOfRequestedCamera(int facing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == facing) {
                return cameraId;
            }
        }
        return -1;
    }


    @SuppressLint("InlinedApi")
    private byte[] createPreviewBuffer(Size previewSize) {
        int bitsPerPixel = ImageFormat.getBitsPerPixel(ImageFormat.NV21);
        long sizeInBits = (long) previewSize.getHeight() * previewSize.getWidth() * bitsPerPixel;
        int bufferSize = (int) Math.ceil(sizeInBits / 8.0d) + 1;

        byte[] byteArray = new byte[bufferSize];
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        if (!buffer.hasArray() || (buffer.array() != byteArray)) {
            throw new IllegalStateException("Failed to create valid buffer for camera source.");
        }

        bytesToByteBuffer.put(byteArray, buffer);
        return byteArray;
    }

    //Size for Preview and Picture
    private static class SizePair {
        private final Size preview;
        private Size picture;

        SizePair(Camera.Size previewSize, @Nullable Camera.Size pictureSize) {
            preview = new Size(previewSize.width, previewSize.height);
            if (pictureSize != null) {
                picture = new Size(pictureSize.width, pictureSize.height);
            }
        }

        Size previewSize() {
            return preview;
        }

        @Nullable
        Size pictureSize() {
            return picture;
        }
    }


    //Frame Processor
    private class CameraPreviewCallback implements Camera.PreviewCallback {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Timber.i("new camera frame %s", data);
            processingRunnable.setNextFrame(data, camera);
        }
    }

    public void setMachineLearningFrameProcessor(VisionImageProcessor processor) {
        synchronized (processorLock) {
            cleanScreen();
            if (frameProcessor != null) {
                frameProcessor.stop();
            }
            frameProcessor = processor;
        }
    }


    private class FrameProcessingRunnable implements Runnable {
        private final Object lock = new Object();
        private boolean active = true;

        private ByteBuffer pendingFrameData;

        FrameProcessingRunnable() {
        }

        @SuppressLint("InlinedApi")
        @SuppressWarnings("GuardedBy")
        @Override
        public void run() {

            ByteBuffer data;

            while (true) {
                //getting the next frame
                synchronized (lock) {
                    //waiting for next frame
                    while (active && (pendingFrameData == null)) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Timber.e(e, "Frame processing loop terminated.");
                            return;
                        }
                    }

                    //stop when not active
                    if (!active) {
                        return;
                    }

                    data = pendingFrameData;
                    pendingFrameData = null;
                }

                try {
                    synchronized (processorLock) {
                        Timber.d("Process an image");
                        frameProcessor.process(
                                data,
                                new FrameMetadata.Builder()
                                        .setWidth(previewSize.getWidth())
                                        .setHeight(previewSize.getHeight())
                                        .setRotation(rotation)
                                        .setCameraFacing(facing)
                                        .build(),
                                overlay);
                    }
                } catch (Throwable t) {
                    Timber.e(t, "Exception thrown from receiver.");
                } finally {
                    camera.addCallbackBuffer(data.array());
                }
            }
        }


        @SuppressLint("Assert")
        void release() {
            assert (processingThread.getState() == Thread.State.TERMINATED);
        }

        void setActive(boolean active) {
            synchronized (lock) {
                this.active = active;
                lock.notifyAll();
            }
        }

        void setNextFrame(byte[] data, Camera camera) {
            synchronized (lock) {
                if (pendingFrameData != null) {
                    camera.addCallbackBuffer(pendingFrameData.array());
                    pendingFrameData = null;
                }

                if (!bytesToByteBuffer.containsKey(data)) {
                    Timber.d("Skipping frame. Could not find ByteBuffer associated with the image data from the camera.");
                    return;
                }

                pendingFrameData = bytesToByteBuffer.get(data);
                lock.notifyAll();
            }
        }
    }


}
