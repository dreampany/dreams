/*
package com.dreampany.word.data.source.pref;

import android.content.Context;

import com.dreampany.language.Language;
import com.dreampany.frame.data.source.pref.FramePref;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.misc.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

*/
/**
 * Created by Hawladar Roman on 3/7/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

@Singleton
public class Pref extends FramePref {

    private final String KEY_WORD_SYNC;

    @Inject
    Pref(Context context) {
        super(context);
        KEY_WORD_SYNC = TextUtil.getString(context, R.string.key_word_sync);
    }

    public boolean hasNotification() {
        return hasWordSync();
    }

    public boolean hasWordSync() {
        return getPublicly(KEY_WORD_SYNC, Boolean.class, true);
    }

    public void commitLoaded() {
        setPrivately(Constants.Word.LOADED, true);
    }

    public boolean isLoaded() {
        return getPrivately(Constants.Word.LOADED, false);
    }

    public void setRecentWord(Word word) {
        setPrivately(Constants.Word.RECENT_WORD, word);
    }

    public Word getRecentWord() {
        return getPrivately(Constants.Word.RECENT_WORD, Word.class, null);
    }

    public void setLanguage(Language language) {
        setPrivately(Constants.Language.LANGUAGE, language);
    }

    public Language getLanguage(Language language) {
        return getPrivately(Constants.Language.LANGUAGE, Language.class, language);
    }

    public void commitLastWordSyncTime() {
        setPrivately(Constants.Pref.WORD_SYNC, TimeUtil.currentTime());
    }

    public long getLastWordSyncTime() {
        return getPrivately(Constants.Pref.WORD_SYNC, 0L);
    }
}
*/
