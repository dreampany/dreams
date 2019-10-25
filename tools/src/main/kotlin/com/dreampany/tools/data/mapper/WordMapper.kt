package com.dreampany.tools.data.mapper

import com.dreampany.firebase.FirebasePref
import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.util.DataUtilKt
import com.dreampany.framework.util.TimeUtil
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.api.wordnik.model.WordnikWord
import com.dreampany.tools.data.model.*
import com.dreampany.tools.data.source.api.WordDataSource
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.injector.annotation.WordAnnote
import com.dreampany.tools.injector.annotation.WordItemAnnote
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.WordItem
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WordMapper
@Inject constructor(
    private val firebasePref: FirebasePref,
    private val pref: WordPref,
    @WordAnnote private val map: SmartMap<String, Word>,
    @WordAnnote private val cache: SmartCache<String, Word>,
    @WordItemAnnote private val uiMap: SmartMap<String, WordItem>,
    @WordItemAnnote private val uiCache: SmartCache<String, WordItem>
) : Mapper() {

    fun commitSyncExpiredTime() {
        pref.commitSyncTime()
    }

    fun isSyncExpired(threshold: Boolean): Boolean {
        val lastTime = pref.getSyncTime()
        val syncTime = getSyncTime(threshold)
        return TimeUtil.isExpired(lastTime, syncTime)
    }

    fun commitTrackExpiredTime() {
        pref.commitTrackTime()
    }

    fun isTrackExpired(): Boolean {
        val lastTime = pref.getTrackTime()
        return TimeUtil.isExpired(lastTime, Constants.Time.Word.TRACK)
    }

    fun commitFirebaseTrackExpiredTime() {
        firebasePref.commitExceptionTime()
    }

    fun isFirebaseTrackExpired(): Boolean {
        val lastTime = firebasePref.getExceptionTime()
        return TimeUtil.isExpired(lastTime, Constants.Time.FIREBASE)
    }

    fun isExists(id: String): Boolean {
        return map.contains(id)
    }

    fun isExists(item: Word): Boolean {
        return map.contains(item.id)
    }

    fun putItem(word: Word): Long {
        map.put(word.id, word)
        return 1L
    }

    fun putItemRx(word: Word): Maybe<Long> {
        return Maybe.create { emitter ->
            val result = putItem(word)
            if (emitter.isDisposed) {
                return@create
            }
            emitter.onSuccess(result)
        }
    }

    fun getItem(word: String?): Word? {
        if (word.isNullOrEmpty()) {
            return null
        }
        var out: Word? = map.get(word)
        if (out == null) {
            out = Word(word)
        }
        return out
    }

    fun getUiItem(id: String): WordItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: WordItem) {
        uiMap.put(id, uiItem)
    }

    fun getItemRx(word: String?): Maybe<Word> {
        return Maybe.create { emitter ->
            val result = getItem(word)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    fun getItem(input: WordnikWord?, full: Boolean): Word? {
        if (input == null) {
            return null
        }

        val id = input.word
        var out: Word? = map.get(id)
        if (out == null) {
            out = Word(id)
            if (full) {
                map.put(id, out)
            }
        }
        out.setPartOfSpeech(input.partOfSpeech)
        out.pronunciation = input.pronunciation
        if (full) {
            out.definitions = getDefinitions(input)
            out.examples = getExamples(input)
            out.synonyms = getSynonyms(input)
            out.antonyms = getAntonyms(input)
        }

/*        if (`in`!!.getWord() == null) {
            val e = NullPointerException()
            Timber.e(e)
        }*/

        return out
    }

    fun getItem(word: String, input: WordnikWord?, full: Boolean): Word? {
        if (input == null) {
            return null
        }

        var out: Word? = map.get(word)
        if (out == null) {
            out = Word(word)
            if (full) {
                map.put(word, out)
            }
        }
        out.setPartOfSpeech(input.partOfSpeech)
        out.pronunciation = input.pronunciation
        if (full) {
            out.definitions = getDefinitions(input)
            out.examples = getExamples(input)
            out.synonyms = getSynonyms(input)
            out.antonyms = getAntonyms(input)
        }

/*        if (`in`!!.getWord() == null) {
            val e = NullPointerException()
            Timber.e(e)
        }*/

        return out
    }

    @Throws(Throwable::class)
    fun getItem(input: Store, source: WordDataSource, full: Boolean = false): Word? {
        var out: Word? = map.get(input.id)
        if (out == null) {
            out = source.getItem(input.id)
            if (full) {
                map.put(input.id, out)
            }
        }
        if (out == null) return null
/*        if (full) {
            out.definitions = getDefinitions(input)
            out.examples = getExamples(input)
            out.synonyms = getSynonyms(input)
            out.antonyms = getAntonyms(input)
        }*/
        return out
    }

    fun getSynonyms(input: Word): ArrayList<Synonym>? {
        var result: ArrayList<Synonym>? = null
        if (input.hasSynonyms()) {
            result = ArrayList()
            input.synonyms?.forEach { item ->
                result.add(Synonym(left = input.id, right = item))
            }
        }
        return result
    }

    fun getSynonyms(word: Word, input: List<Synonym>?): ArrayList<String>? {
        var result: ArrayList<String>? = null
        if (!input.isNullOrEmpty()) {
            result = ArrayList()
            input.forEach { item ->
                if (word.id.equals(item.left)) {
                    result.add(item.right)
                } else {
                    result.add(item.left)
                }
            }
        }
        return result
    }

    fun getAntonyms(input: Word): ArrayList<Antonym>? {
        var result: ArrayList<Antonym>? = null
        if (input.hasAntonyms()) {
            result = ArrayList()
            input.antonyms?.forEach { item ->
                result.add(Antonym(left = input.id, right = item))
            }
        }
        return result
    }

    fun getAntonyms(word: Word, input: List<Antonym>?): ArrayList<String>? {
        var result: ArrayList<String>? = null
        if (!input.isNullOrEmpty()) {
            result = ArrayList()
            input.forEach { item ->
                if (word.id.equals(item.left)) {
                    result.add(item.right)
                } else {
                    result.add(item.left)
                }
            }
        }
        return result
    }

    private fun getDefinitions(input: WordnikWord): ArrayList<Definition>? {
        if (input.hasDefinition()) {
            val result = ArrayList<Definition>()
            input.definitions?.forEach { item ->
                if (!item.text.isNullOrEmpty()) {
                    result.add(
                        Definition(
                            time = TimeUtilKt.currentMillis(),
                            id = if (item.id.isNullOrEmpty()) DataUtilKt.getRandId() else item.id,
                            partOfSpeech = item.partOfSpeech,
                            text = item.text,
                            url = item.wordnikUrl
                        )
                    )
                }
            }
            return result
        }
        return null
    }

    private fun getExamples(input: WordnikWord): ArrayList<Example>? {
        if (input.hasExample()) {
            val result = ArrayList<Example>()
            input.examples?.forEach { exm ->
                result.add(
                    Example(
                        documentId = exm.documentId,
                        exampleId = exm.exampleId,
                        author = exm.author,
                        title = exm.title,
                        text = exm.text,
                        url = exm.url,
                        year = exm.year,
                        rating = exm.rating
                    )
                )
            }
            return result
        }
        return null
    }

    private fun getSynonyms(input: WordnikWord): ArrayList<String>? {
        if (input.hasSynonyms()) {
            val result = ArrayList<String>()
            input.synonyms?.forEach { synonym ->
                val synonym = synonym.toLowerCase()
                if (!result.contains(synonym)) {
                    result.add(synonym)
                }
            }
            return result
        }
        return null
    }

    private fun getAntonyms(input: WordnikWord): ArrayList<String>? {
        if (input.hasAntonyms()) {
            val result = ArrayList<String>()
            input.antonyms?.forEach { antonym ->
                val antonym = antonym.toLowerCase()
                if (!result.contains(antonym)) {
                    result.add(antonym)
                }
            }
            return result
        }
        return null
    }

    private fun getSyncTime(threshold: Boolean): Long {
        if (!threshold) return Constants.Time.Word.SYNC_DEAD
        val count = pref.getSyncedCount()
        if (count < Constants.Count.Word.SYNC_FREQUENT) return Constants.Time.Word.SYNC_FREQUENT
        if (count < Constants.Count.Word.SYNC_NORMAL) return Constants.Time.Word.SYNC_NORMAL
        return Constants.Time.Word.SYNC_LAZY
    }
}