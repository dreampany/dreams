package com.dreampany.vision.ml.ocr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dreampany.vision.ml.FrameMetadata;
import com.dreampany.vision.ml.GraphicOverlay;
import com.dreampany.vision.ml.VisionProcessor;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 9/28/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class TextRecognitionProcessor extends VisionProcessor<FirebaseVisionText> {

    public interface Callback {
        void onText(String text);
    }

    private final FirebaseVisionTextRecognizer detector;
    private final Callback callback;

    private final StringBuilder textBuilder;

    public TextRecognitionProcessor(@Nullable Callback callback) {
        this.callback = callback;
        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        textBuilder = new StringBuilder();
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Timber.e(e, "Exception thrown while trying to close Text Detector.");
        }
    }

    @Override
    protected Task<FirebaseVisionText> detectInImage(FirebaseVisionImage image) {
        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(@NonNull FirebaseVisionText result, @NonNull FrameMetadata meta, @NonNull GraphicOverlay overlay) {
        overlay.clear();
        List<FirebaseVisionText.TextBlock> blocks = result.getTextBlocks();
        textBuilder.setLength(0);
        for (FirebaseVisionText.TextBlock block : blocks) {
            textBuilder.append(block.getText());
        }
        if (callback != null) {
            callback.onText(textBuilder.toString());
        }

/*        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    textBuilder.append(elements.get(k).getText()) ;
                   // if (textBuilder.c)
                    *//*GraphicOverlay.Graphic textGraphic = new TextGraphic(overlay, elements.get(k));
                    overlay.add(textGraphic);*//*
                }
            }
        }*/
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Timber.w(e, "Text detection failed.");
    }
}
