package com.dreampany.vision.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.NotifyUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.vision.R;
import com.dreampany.vision.databinding.FragmentLiveTextOcrBinding;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 9/28/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@KeepName
@ActivityScope
public class LiveTextOcrFragment extends BaseMenuFragment {

    FragmentLiveTextOcrBinding binding;

    CameraSource source = null;
    CameraSourcePreview preview;
    GraphicOverlay overlay;
    TextView textView;
    CheckBox check;
    StringBuilder texts = new StringBuilder();

    @Inject
    public LiveTextOcrFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_live_text_ocr;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_live_text_ocr;
    }

    @Override
    public void onMenuCreated(@NotNull Menu menu) {
        MenuItem checkItem = menu.findItem(R.id.item_auto_collection);
        check = (CheckBox) checkItem.getActionView();
        check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String text = isChecked ? "All text collection is enabled" : "All text collection is disabled";
            NotifyUtil.shortToast(getContext(), text);
        });
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();

        Dexter.withActivity(getParent())
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

    @Override
    public void onPause() {
        preview.stop();
        super.onPause();
    }

    @Override
    public boolean hasBackPressed() {
        done();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_clear) {
            clear();
            return true;
        }
        if (item.getItemId() == R.id.item_done) {
            done();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        setTitle(TextUtil.getString(getContext(), R.string.detected_words, 0));
        binding = (FragmentLiveTextOcrBinding) super.binding;
        preview = binding.preview;
        overlay = binding.overlay;
        textView = binding.text;
    }

    private void createCameraSource() {
        if (source == null) {
            source = new CameraSource(getParent(), overlay);
        }
        source.setMachineLearningFrameProcessor(new TextRecognitionProcessor(this::updateTitle));
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

    private void updateTitle(String text) {
        if (!check.isChecked()) {
            texts.setLength(0);
        }
        texts.append(text);
        String result = texts.toString();
        textView.setText(text);
        int words = TextUtil.getWordsCount(result);
        setTitle(TextUtil.getString(getContext(), R.string.detected_words, words));
    }

    private void clear() {
        setTitle(TextUtil.getString(getContext(), R.string.detected_words, 0));
        textView.setText(null);
        texts.setLength(0);
    }

    private void done() {
        getCurrentTask(false).setComment(texts.toString());
        forResult();
    }
}
