package com.dreampany.word.data.source.pref;

import android.content.Context;

import com.dreampany.frame.data.enums.Language;
import com.dreampany.frame.data.source.pref.FramePref;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.misc.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 3/7/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class Pref extends FramePref {


    @Inject
    Pref(Context context) {
        super(context);
    }


    public void setLastSearchWord(Word word) {
        setPrivately(Constants.Word.LAST_SEARCH, word);
    }

    public Word getLastSearchWord() {
        return getPrivately(Constants.Word.LAST_SEARCH, Word.class, null);
    }

    public void setLanguage(Language language) {
        setPrivately(Constants.Language.LANGUAGE, language);
    }

    public Language getLanguage(Language language) {
        return getPrivately(Constants.Language.LANGUAGE, Language.class, language);
    }
}
