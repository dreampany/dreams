package com.dreampany.vision.ml;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.firebase.ml.common.FirebaseMLException;

import java.nio.ByteBuffer;

/**
 * Created by Hawladar Roman on 9/28/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface VisionImageProcessor {
    void process(ByteBuffer data, FrameMetadata meta, GraphicOverlay overlay) throws FirebaseMLException;

    void process(Bitmap bitmap, GraphicOverlay overlay);

    void process(Image image, int rotation, GraphicOverlay overlay);

    void stop();
}
