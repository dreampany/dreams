package com.dreampany.word.data.source.pref

import android.content.Context
import com.dreampany.frame.data.source.pref.FramePrefKt
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.language.Language
import com.dreampany.word.R
import com.dreampany.word.data.model.Word
import com.dreampany.word.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref @Inject constructor(context: Context) : FramePrefKt(context) {

    private val KEY_WORD_SYNC: String

    init {
        KEY_WORD_SYNC = TextUtil.getString(context, R.string.key_word_sync)!!
    }

    fun hasNotification(): Boolean {
        return hasWordSync()
    }

    fun hasWordSync(): Boolean {
        return getPublicly(KEY_WORD_SYNC, true)
    }

    fun commitLoaded() {
        setPrivately(Constants.Word.LOADED, true)
    }

    fun isLoaded(): Boolean {
        return getPrivately(Constants.Word.LOADED, false)
    }

    fun setRecentWord(word: Word) {
        setPrivately(Constants.Word.RECENT_WORD, word)
    }

    fun getRecentWord(): Word? {
        return getPrivately(Constants.Word.RECENT_WORD, Word::class.java, null)
    }

    fun setLanguage(language: Language) {
        setPrivately(Constants.Language.LANGUAGE, language)
    }

    fun getLanguage(language: Language): Language {
        return getPrivately(Constants.Language.LANGUAGE, Language::class.java, language)
    }

    fun commitLastWordSyncTime() {
        setPrivately(Constants.Pref.WORD_SYNC, TimeUtil.currentTime())
    }

    fun getLastWordSyncTime(): Long {
        return getPrivately(Constants.Pref.WORD_SYNC, 0L)
    }
}