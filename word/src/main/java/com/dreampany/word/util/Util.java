package com.dreampany.word.util;

import androidx.annotation.NonNull;

import com.dreampany.frame.util.DataUtil;
import com.dreampany.word.data.model.Definition;
import com.dreampany.word.data.model.Word;

/**
 * Created by Hawladar Roman on 9/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class Util {
    private Util () {}

    public static String getText(@NonNull Word word) {
        StringBuilder builder = new StringBuilder();
        if (word.hasDefinitions()) {
            for (Definition item : word.getDefinitions()) {
                DataUtil.joinString(builder, item.getText());
            }
        }

        if (word.hasExamples()) {
            for (String item : word.getExamples()) {
                DataUtil.joinString(builder, item);
            }
        }

        if (word.hasSynonyms()) {
            for (String item : word.getSynonyms()) {
                DataUtil.joinString(builder, item);
            }
        }

        if (word.hasAntonyms()) {
            for (String item : word.getAntonyms()) {
                DataUtil.joinString(builder, item);
            }

        }

        String text = builder.toString();
        return text;
    }
}
