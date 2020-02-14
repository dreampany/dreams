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

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.WORD
    }

    fun commitTrackLoaded() {
        setPrivately(Constants.Pref.WORD_TRACK_LOADED, true)
    }

    fun commitCommonLoaded() {
        setPrivately(Constants.Pref.WORD_COMMON_LOADED, true)
    }

    fun commitAlphaLoaded() {
        setPrivately(Constants.Pref.WORD_ALPHA_LOADED, true)
    }

    fun isTrackLoaded(): Boolean {
        return getPrivately(Constants.Pref.WORD_TRACK_LOADED, Constants.Default.BOOLEAN)
    }

    fun isCommonLoaded(): Boolean {
        return getPrivately(Constants.Pref.WORD_COMMON_LOADED, Constants.Default.BOOLEAN)
    }

    fun isAlphaLoaded(): Boolean {
        return getPrivately(Constants.Pref.WORD_ALPHA_LOADED, Constants.Default.BOOLEAN)
    }

    fun setTrackStartAt(startAt: String) {
        return setPrivately(Constants.Pref.WORD_TRACK_START_AT, startAt)
    }

    fun getTrackStartAt(): String {
        return getPrivately(Constants.Pref.WORD_TRACK_START_AT, Constants.Default.STRING)
    }

    fun commitTrackTime() {
        return setTrackTime(TimeUtilKt.currentMillis())
    }

    fun setTrackTime(time: Long) {
        return setPrivately(Constants.Pref.Word.TIME_TRACK, time)
    }

    fun getTrackTime(): Long {
        return getPrivately(Constants.Pref.Word.TIME_TRACK, Constants.Default.LONG)
    }

    fun setTrackCount(count: Int) {
        return setPrivately(Constants.Pref.WORD_TRACK_COUNT, count)
    }

    fun getTrackCount(): Int {
        return getPrivately(Constants.Pref.WORD_TRACK_COUNT, Constants.Default.INT)
    }

    fun setLastWord(item: Word) {
        setPrivately(Constants.Pref.WORD_LAST, item)
    }

    fun clearLastWord() {
        removePrivately(Constants.Pref.WORD_LAST)
    }

    fun getLastWord(): Word? {
        return getPrivately(Constants.Pref.WORD_LAST, Word::class.java, Constants.Default.NULL)
    }

    fun setRecentWord(word: Word) {
        setPrivately(Constants.Keys.Word.RECENT_WORD, word)
    }

    fun getRecentWord(): Word? {
        return getPrivately(Constants.Keys.Word.RECENT_WORD, Word::class.java, Constants.Default.NULL)
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

    fun commitLoadTime() {
        setPrivately(Constants.Pref.Word.TIME_SYNC, TimeUtilKt.currentMillis())
    }

    fun getLoadTime(): Long {
        return getPrivately(Constants.Pref.Word.TIME_SYNC, Constants.Default.LONG)
    }

    fun commitSyncTime() {
        setPrivately(Constants.Pref.Word.TIME_SYNC, TimeUtilKt.currentMillis())
    }

    fun getSyncTime(): Long {
        return getPrivately(Constants.Pref.Word.TIME_SYNC, Constants.Default.LONG)
    }

    fun setSyncedCount(count:Long) {
        setPrivately(Constants.Pref.Word.COUNT_SYNCED, count)
    }

    fun getSyncedCount(defaultCount: Long = Constants.Default.LONG): Long {
       return getPrivately(Constants.Pref.Word.COUNT_SYNCED, defaultCount)
    }
}