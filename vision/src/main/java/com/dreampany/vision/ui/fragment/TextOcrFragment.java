package com.dreampany.vision.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.vision.OcrDetectorProcessor;
import com.dreampany.vision.OcrGraphic;
import com.dreampany.vision.R;
import com.dreampany.vision.databinding.FragmentTextOcrBinding;
import com.dreampany.vision.ui.activity.TextOcrActivity;
import com.dreampany.vision.ui.camera.CameraSource;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by air on 10/25/17.
 */
@ActivityScope
public class TextOcrFragment extends BaseMenuFragment implements ScaleGestureDetector.OnScaleGestureListener, PermissionListener {

    private static final int RC_HANDLE_GMS = 9001;

    private CameraSource cameraSource;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private FragmentTextOcrBinding binding;
    private Set<String> words;

    private boolean autoFocus;
    private boolean useFlash;

    @Inject
    public TextOcrFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_text_ocr;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_done;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        binding = (FragmentTextOcrBinding) super.binding;

        gestureDetector = new GestureDetector(getContext(), new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(getContext(), this);

        autoFocus = getActivity().getIntent().getBooleanExtra(TextOcrActivity.AUTO_FOCUS, false);
        useFlash = getActivity().getIntent().getBooleanExtra(TextOcrActivity.USE_FLASH, false);

        if (AndroidUtil.hasMarshmallow()) {
            Dexter.withActivity(getActivity())
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(this)
                    .check();
        } else {
            createCameraSource(autoFocus, useFlash);
        }

        Snackbar.make(binding.graphicOverlay, "Tap to capture. Pinch/Stretch to zoom",
                Snackbar.LENGTH_INDEFINITE)
                .show();

        AndroidUtil.initTts(getApp());
        words = new HashSet<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.preview.stop();

    }

    @Override
    protected void onStopUi() {
        binding.preview.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_done) {
            done();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        createCameraSource(autoFocus, useFlash);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (cameraSource != null) {
            cameraSource.doZoom(detector.getScaleFactor());
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean b = scaleGestureDetector.onTouchEvent(event);

        boolean c = gestureDetector.onTouchEvent(event);

        return b || c /*|| super.onTouchEvent(event)*/;
    }

    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {

        Context context = getApp();
        TextRecognizer recognizer = new TextRecognizer.Builder(context).build();
        recognizer.setProcessor(new OcrDetectorProcessor(binding.graphicOverlay));

        if (!recognizer.isOperational()) {
            IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = getParent().registerReceiver(null, lowStorageFilter) != null;

            if (hasLowStorage) {
                Timber.e("Low storage to download gms detection native libraries");
            }
        }

        if (cameraSource == null) {
            cameraSource =
                    new CameraSource.Builder(context, recognizer)
                            .setFacing(CameraSource.CAMERA_FACING_BACK)
                            .setRequestedPreviewSize(1280, 1024)
                            .setRequestedFps(2.0f)
                            .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                            .setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null)
                            .build();
        }
    }

    @SuppressLint("MissingPermission")
    private void startCameraSource() throws SecurityException {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApp());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(getParent(), code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (cameraSource != null) {
            try {
                binding.preview.start(cameraSource, binding.graphicOverlay);
            } catch (IOException e) {
                Timber.v("Unable to start camera source. %s", e.toString());
                cameraSource.release();
                cameraSource = null;
            }
        }
    }


    private boolean onTap(float rawX, float rawY) {
        OcrGraphic graphic = (OcrGraphic) binding.graphicOverlay.getGraphicAtLocation(rawX, rawY);
        TextBlock text = null;

        if (graphic != null) {
            text = graphic.getTextBlock();
            if (text != null && text.getValue() != null) {
                AndroidUtil.speak(text.getValue());
                buildWords(text.getValue());
                Timber.v("text data is being spoken! %s", text.getValue());
            } else {
                Timber.v("text data is null");
            }
        } else {
            Timber.v("no text detected");
        }

        return text != null;
    }

    private final class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private void buildWords(String text) {
        words.addAll(TextUtil.getWords(text));
    }

    private void done() {
        Intent data = new Intent();
        data.putExtra(TextOcrActivity.TEXT_BLOCK_OBJECT, new ArrayList<>(words));
        getParent().setResult(CommonStatusCodes.SUCCESS, data);
        getParent().finish();
    }
}
