package com.dreampany.vision;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by air on 10/22/17.
 */
@Singleton
public class VisionApi {

    private final Context context;

    @Inject
    VisionApi(Context context) {
        this.context = context.getApplicationContext();
    }

    public String getText(Bitmap bitmap) throws Exception {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> task = recognizer.processImage(image);
        if (task.isSuccessful()) {
            FirebaseVisionText text = task.getResult();
            StringBuilder result = new StringBuilder();
            for (FirebaseVisionText.TextBlock block : text.getTextBlocks()) {
                result.append(block.getText());
            }
            return result.toString();
        } else {
            Exception exception = task.getException();
            Timber.e(exception);
            throw exception;
        }
    }

    public String recognize(Bitmap bitmap) {
        if (bitmap == null) {
            Timber.e("Text Ocr bitmap is null");
            return null;
        }
        StringBuilder result = new StringBuilder();
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        try {
            if (!textRecognizer.isOperational()) {
                /*new AlertDialog.
                        Builder(context).
                        setMessage().show();*/
                //NotifyUtil.showError(context, "Text recognizer could not be set up on your device");
                Timber.e("Text recognizer could not be set up on your device");
                return null;
            }

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> origTextBlocks = textRecognizer.detect(frame);
            List<TextBlock> textBlocks = new ArrayList<>();
            for (int i = 0; i < origTextBlocks.size(); i++) {
                TextBlock textBlock = origTextBlocks.valueAt(i);
                textBlocks.add(textBlock);
            }
            Collections.sort(textBlocks, (o1, o2) -> {
                int diffOfTops = o1.getBoundingBox().top - o2.getBoundingBox().top;
                int diffOfLefts = o1.getBoundingBox().left - o2.getBoundingBox().left;
                if (diffOfTops != 0) {
                    return diffOfTops;
                }
                return diffOfLefts;
            });

            for (TextBlock textBlock : textBlocks) {
                if (textBlock != null && textBlock.getValue() != null) {
                    result.append(textBlock.getValue());
                    result.append("\n");
                }
            }

        } finally {
            textRecognizer.release();
        }

        return result.toString();
    }
}
