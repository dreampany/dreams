package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.misc.Constants
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 8/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WordPref
@Inject constructor(
    context: Context
) : FramePref(context) {

    override fun getPrivatePrefName(context: Context): String? {
        return Constants.Pref.WORD
    }

    fun commitCommonLoaded() {
        setPrivately(Constants.Pref.WORD_COMMON_LOADED, true)
    }

    fun commitAlphaLoaded() {
        setPrivately(Constants.Pref.WORD_ALPHA_LOADED, true)
    }

    fun isCommonLoaded(): Boolean {
        return getPrivately(Constants.Pref.WORD_COMMON_LOADED, false)
    }

    fun isAlphaLoaded(): Boolean {
        return getPrivately(Constants.Pref.WORD_ALPHA_LOADED, false)
    }

    fun setLastWord(item: Word) {
        setPrivately(Constants.Pref.WORD_LAST, item)
    }

    fun clearLastWord() {
        removePrivately(Constants.Pref.WORD_LAST)
    }

    fun getLastWord(): Word? {
        return getPrivately(Constants.Pref.WORD_LAST, Word::class.java, null)
    }

    fun setRecentWord(word: Word) {
        setPrivately(Constants.Word.RECENT_WORD, word)
    }

    fun getRecentWord(): Word? {
        return getPrivately(Constants.Word.RECENT_WORD, Word::class.java, null)
    }

    fun getRecentWordRx(): Maybe<Word> {
        return Maybe.create { emitter ->
            val word = getRecentWord()
            if (emitter.isDisposed) {
                return@create
            }
            if (word == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(word)
            }
        }
    }

    fun commitLastWordSyncTime() {
        setPrivately(Constants.Pref.WORD_LAST_SYNC_TIME, TimeUtilKt.currentMillis())
    }

    fun getLastWordSyncTime(): Long {
        return getPrivately(Constants.Pref.WORD_LAST_SYNC_TIME, 0L)
    }
}