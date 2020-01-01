package com.dreampany.vision.ml;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Hawladar Roman on 9/28/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public abstract class VisionProcessor<T> implements VisionImageProcessor {

    private final AtomicBoolean shouldThrottle = new AtomicBoolean(false);

    public VisionProcessor() {
    }

    protected abstract Task<T> detectInImage(FirebaseVisionImage image);

    protected abstract void onSuccess(@NonNull T result, @NonNull FrameMetadata meta, @NonNull GraphicOverlay overlay);

    protected abstract void onFailure(@NonNull Exception e);

    @Override
    public void process(ByteBuffer data, FrameMetadata meta, GraphicOverlay overlay) throws FirebaseMLException {
        if (shouldThrottle.get()) {
            return;
        }
        FirebaseVisionImageMetadata metadata =
                new FirebaseVisionImageMetadata.Builder()
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .setWidth(meta.getWidth())
                        .setHeight(meta.getHeight())
                        .setRotation(meta.getRotation())
                        .build();

        detectInVisionImage(FirebaseVisionImage.fromByteBuffer(data, metadata), meta, overlay);
    }

    @Override
    public void process(Bitmap bitmap, GraphicOverlay overlay) {
        if (shouldThrottle.get()) {
            return;
        }
        detectInVisionImage(FirebaseVisionImage.fromBitmap(bitmap), null, overlay);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void process(Image image, int rotation, GraphicOverlay overlay) {
        if (shouldThrottle.get()) {
            return;
        }
        FrameMetadata meta = new FrameMetadata.Builder()
                .setWidth(image.getWidth())
                .setHeight(image.getHeight())
                .build();
        FirebaseVisionImage visionImage = FirebaseVisionImage.fromMediaImage(image, rotation);
        detectInVisionImage(visionImage, meta, overlay);
    }

    @Override
    public void stop() {

    }

    private void detectInVisionImage(FirebaseVisionImage image, FrameMetadata meta, GraphicOverlay overlay) {
        detectInImage(image)
                .addOnSuccessListener(result -> {
                    shouldThrottle.set(false);
                    VisionProcessor.this.onSuccess(result, meta, overlay);
                })
                .addOnFailureListener(e -> {
                    shouldThrottle.set(false);
                    VisionProcessor.this.onFailure(e);
                });

        shouldThrottle.set(true);
    }
}
