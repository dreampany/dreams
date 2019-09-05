package com.dreampany.word.data.source.pref;

import android.content.Context;

import com.dreampany.framework.data.source.pref.BasePref;
import com.dreampany.word.data.model.Word;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 9/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class LoadPref extends BasePref {

    private static final String COMMON_LOADED = "common_loaded";
    private static final String ALPHA_LOADED = "alpha_loaded";
    private static final String LAST_WORD = "last_word";
    private static final String DEFAULT_FLAG_COMMITTED = "default_flag_committed";
    private static final String RECENT_COUNT = "recent_count";

    @Inject
    LoadPref(Context context) {
        super(context);
    }

    public void commitCommonLoaded() {
        setPrivately(COMMON_LOADED, true);
    }

    public void commitAlphaLoaded() {
        setPrivately(ALPHA_LOADED, true);
    }

    public boolean isCommonLoaded() {
        return getPrivately(COMMON_LOADED, false);
    }

    public boolean isAlphaLoaded() {
        return getPrivately(ALPHA_LOADED, false);
    }

    public void setLastWord(Word item) {
        setPrivately(LAST_WORD, item);
    }

    public void clearLastWord() {
        removePrivately(LAST_WORD);
    }

    public Word getLastWord() {
        return getPrivately(LAST_WORD, Word.class, null);
    }

    synchronized public void commitDefaultFlag() {
        setPrivately(DEFAULT_FLAG_COMMITTED, true);
    }

    synchronized public boolean isDefaultFlagCommitted() {
        return getPrivately(DEFAULT_FLAG_COMMITTED, false);
    }

    synchronized public int getRecentCount() {
        return getPrivately(RECENT_COUNT, 0);
    }
}
