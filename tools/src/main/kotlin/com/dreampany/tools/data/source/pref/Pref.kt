package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.frame.data.source.pref.FramePref
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.language.Language
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.misc.Constants
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref
@Inject constructor(
    context: Context
) : FramePref(context) {

    init {
    }

    fun hasNotification(): Boolean {
        return true
    }

    fun setLanguage(language: Language) {
        setPrivately(Constants.Language.LANGUAGE, language)
    }

    fun getLanguage(language: Language): Language {
        return getPrivately(Constants.Language.LANGUAGE, Language::class.java, language)
    }



    fun setRecentWord(word: Word) {
        setPrivately(Constants.Word.RECENT_WORD, word)
    }

    fun getRecentWord(): Word? {
        return getPrivately(Constants.Word.RECENT_WORD, Word::class.java, null)
    }

    fun getRecentWordRx(): Maybe<Word> {
        return Maybe.create {emitter ->
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
}