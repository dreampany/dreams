package com.dreampany.word.api.wordnik;

import com.dreampany.frame.util.DataUtil;

/**
 * Created by Hawladar Roman on 9/3/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class WordnikDefinition {

    private String partOfSpeech;
    private String text;

    public WordnikDefinition(String partOfSpeech, String text) {
        this.partOfSpeech = partOfSpeech;
        this.text = text;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getText() {
        return text;
    }

    public boolean isEmpty() {
        return DataUtil.isEmpty(partOfSpeech) || DataUtil.isEmpty(text);
    }
}
