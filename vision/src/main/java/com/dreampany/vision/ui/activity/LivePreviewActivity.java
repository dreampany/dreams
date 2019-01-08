package com.dreampany.vision.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dreampany.frame.ui.activity.BaseActivity;
import com.dreampany.vision.R;
import com.dreampany.vision.databinding.ActivityLivePreviewBinding;
import com.dreampany.vision.ml.CameraSource;
import com.dreampany.vision.ml.CameraSourcePreview;
import com.dreampany.vision.ml.GraphicOverlay;
import com.dreampany.vision.ml.ocr.TextRecognitionProcessor;
import com.google.android.gms.common.annotation.KeepName;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 9/28/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@KeepName
public final class LivePreviewActivity extends BaseActivity {

    ActivityLivePreviewBinding binding;

    private CameraSource source = null;
    private CameraSourcePreview preview;
    private GraphicOverlay overlay;


    @Override
    public int getLayoutId() {
        return R.layout.activity_live_preview;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            createCameraSource();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

    }

    @Override
    protected void onStopUi() {
        if (source != null) {
            source.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    private void initView() {
        binding = (ActivityLivePreviewBinding) super.binding;
        preview = binding.preview;
        overlay = binding.overlay;
    }

    private void createCameraSource() {
        if (source == null) {
            source = new CameraSource(this, overlay);
        }
        source.setMachineLearningFrameProcessor(new TextRecognitionProcessor(null));
    }

    private void startCameraSource() {
        if (source != null) {
            try {
                if (preview == null) {
                    Timber.d("resume: Preview is null");
                }
                if (overlay == null) {
                    Timber.d("resume: graphOverlay is null");
                }
                preview.start(source, overlay);
            } catch (IOException e) {
                Timber.e(e, "Unable to start camera source.");
                source.release();
                source = null;
            }
        }
    }
}
