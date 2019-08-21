package com.dreampany.tools.data.misc

import com.dreampany.frame.data.model.Store
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.TextUtil
import com.dreampany.tools.api.wordnik.model.WordnikWord
import com.dreampany.tools.data.model.Antonym
import com.dreampany.tools.data.model.Definition
import com.dreampany.tools.data.model.Synonym
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.api.WordDataSource
import com.dreampany.tools.misc.WordAnnote
import com.dreampany.tools.misc.WordItemAnnote
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
    @WordAnnote val map: SmartMap<String, Word>,
    @WordAnnote val cache: SmartCache<String, Word>,
    @WordItemAnnote val uiMap: SmartMap<String, WordItem>,
    @WordItemAnnote val uiCache: SmartCache<String, WordItem>
) {

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

        val id = input.word!!
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

    fun toItemFromStore(input: Store, source: WordDataSource): Word? {
        var out: Word? = map.get(input.id)
        if (out == null) {
            out = source.getItem(input.id)
            map.put(input.id, out)
        }
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

    fun getAntonyms(word: Word, input: List<Antonym>?): List<String>? {
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
                val def = Definition()
                def.setPartOfSpeech(item.partOfSpeech)
                def.text = TextUtil.stripHtml(item.text)
                result.add(def)
            }
            return result
        }
        return null
    }

    private fun getExamples(input: WordnikWord): ArrayList<String>? {
        if (input.hasExample()) {
            val result = ArrayList<String>()
            input.examples?.forEach {
                result.add(it)
            }
            return result
        }
        return null
    }

    private fun getSynonyms(input: WordnikWord): ArrayList<String>? {
        if (input.hasSynonyms()) {
            val result = ArrayList<String>()
            input.synonyms?.forEach {
                result.add(it)
            }
            return result
        }
        return null
    }

    private fun getAntonyms(input: WordnikWord): ArrayList<String>? {
        if (input.hasAntonyms()) {
            val result = ArrayList<String>()
            input.antonyms?.forEach {
                result.add(it)
            }
            return result
        }
        return null
    }
}