package com.dreampany.tools.api.wordnik

import com.dreampany.frame.util.*
import com.dreampany.tools.api.wordnik.core.ClientException
import com.dreampany.tools.api.wordnik.misc.Constants
import com.dreampany.tools.api.wordnik.model.*
import com.google.common.collect.Maps
import org.apache.commons.collections4.queue.CircularFifoQueue
import org.apache.commons.lang3.tuple.MutablePair
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Created by roman on 2019-08-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WordnikManager
@Inject constructor() {

    private val keys = arrayOf(
        Constants.ApiKey.WORDNIK_API_KEY_ROMANBJIT,
        Constants.ApiKey.WORDNIK_API_KEY_DREAMPANY,
        Constants.ApiKey.WORDNIK_API_KEY_IFTENET
    )

    private val wordsApis: MutableList<WordsApi>
    private val wordApis: MutableList<WordApi>

    private val indexQueue: CircularFifoQueue<Int>
    private val indexStatus: MutableMap<Int, MutablePair<Long, Int>>

    init {
        wordsApis = Collections.synchronizedList(ArrayList<WordsApi>())
        wordApis = Collections.synchronizedList(ArrayList<WordApi>())
        indexQueue = CircularFifoQueue(keys.size)
        indexStatus = Maps.newConcurrentMap()

        for (index in keys.indices) {
            val key = keys[index]
            indexQueue.add(index)
            wordsApis.add(WordsApi(keyOfApi = key))
            wordApis.add(WordApi(keyOfApi = key))
            indexStatus[index] = MutablePair.of(TimeUtilKt.currentMillis(), 0)
        }
        var randIndex = NumberUtil.nextRand(keys.size)
        while (randIndex > 0) {
            iterateQueue()
            randIndex--
        }
    }

    fun getWordOfTheDay(date: String, limit: Int): WordnikWord? {
        for (index in keys.indices) {
            val api = getWordsApi()
            try {
                val wordOfTheDay = api.getWordOfTheDay(date)
                return getWord(wordOfTheDay, limit)
            } catch (error: Throwable) {
                Timber.e(error)
                iterateQueue()
            }

        }
        return null
    }

    fun getWord(word: String, limit: Int): WordnikWord? {
        for (index in keys.indices) {
            val api = getWordApi()
            try {
                val useCanonical = "true"
                val includeSuggestions = "false"
                //val word = api.getWord(word, useCanonical, includeSuggestions);
                return getWordImpl(word, limit)
            } catch (error: Throwable) {
                Timber.e(error)
                iterateQueue()
            }

        }

        return null
    }

    fun search(query: String, limit: Int): List<WordnikWord>? {

        val includePartOfSpeech: String? = null
        val excludePartOfSpeech: String? = null
        val caseSensitive = "false"
        val minCorpusCount = 5
        val maxCorpusCount = -1
        val minDictionaryCount = 1
        val maxDictionaryCount = -1
        val minLength = 1
        val maxLength = -1
        val skip = 0

        for (index in keys.indices) {
            val api = getWordsApi()
            /*            try {
                WordSearchResults results = api.searchWords(query,
                        includePartOfSpeech,
                        excludePartOfSpeech,
                        caseSensitive,
                        minCorpusCount,
                        maxCorpusCount,
                        minDictionaryCount,
                        maxDictionaryCount,
                        minLength,
                        maxLength,
                        skip,
                        limit
                );
                if (results != null) {
                    List<WordSearchResult> searches = results.getSearchResults();
                    if (!DataUtil.isEmpty(searches)) {
                        List<WordnikWord> words = new ArrayList<>(searches.size());
                        for (WordSearchResult result : searches) {
                            words.add(new WordnikWord(result.getWord().toLowerCase()));
                        }
                        return words;
                    }
                }

            } catch (Exception e) {
                Timber.e(e);
                iterateQueue();
            }*/
        }
        return null
    }

    private fun iterateQueue() {
        indexQueue.add(indexQueue.peek())
    }

    private fun adjustIndexStatus() {
        val index = indexQueue.peek()!!
        val pair = indexStatus[index]
        pair?.let {
            if (TimeUtil.isExpired(it.left, Constants.Delay.WordnikKey)) {
                it.setLeft(TimeUtilKt.currentMillis())
                it.setRight(0)
            }
            if (it.right > Constants.Limit.WORDNIK_KEY) {
                iterateQueue()
            }
            it.right++
        }
    }

    private fun getWordApi(): WordApi {
        adjustIndexStatus()
        return wordApis[indexQueue.peek()!!]
    }

    private fun getWordsApi(): WordsApi {
        adjustIndexStatus()
        return wordsApis[indexQueue.peek()!!]
    }

    private fun getWord(from: WordOfTheDay, limit: Int): WordnikWord {
        val word = WordnikWord(from.word!!.toLowerCase())
        word.partOfSpeech = getPartOfSpeech(from)
        //word.setPronunciation(getPronunciation(from));
        word.definitions = getDefinitions(from)
        word.examples = getExamples(from)

        val relateds = getRelateds(from.word, Constants.Word.SYNONYM_ANTONYM, limit)
        word.synonyms = getSynonyms(relateds)
        word.antonyms = getAntonyms(relateds)
        return word
    }

    private fun getWordImpl(from: String, limit: Int): WordnikWord {
        val word = WordnikWord(from)

        val definitions = getDefinitions(from, limit)

        word.partOfSpeech = getPartOfSpeech(definitions)
        word.definitions = getDefinitions(definitions)
        word.examples = getExamplesBy(definitions)
        word.pronunciation = getPronunciation(from, limit)

        //List<String> examples = getExamples(from, limit);
        //word.setExamples(examples);
        //word.setExamples(getExamples(examples));

        val relateds = getRelateds(from, Constants.Word.SYNONYM_ANTONYM, limit)

        word.synonyms = getSynonyms(relateds)
        word.antonyms = getAntonyms(relateds)

        return word
    }

    private fun getPartOfSpeech(word: WordOfTheDay): String? {
        val items = word.definitions
        if (!DataUtil.isEmpty(items)) {
/*            for (item in items) {
                *//*             if (!DataUtil.isEmpty(item.getPartOfSpeech())) {
                    return item.getPartOfSpeech();
                }*//*
            }*/
        }
        return null
    }

    private fun getPartOfSpeech(items: List<Definition>?): String? {
        if (!DataUtil.isEmpty(items)) {
            for (item in items!!) {
                if (!DataUtil.isEmpty(item.partOfSpeech)) {
                    return item.partOfSpeech
                }
            }
        }
        return null
    }

/*    private String getPronunciation(WordOfTheDay word) {
        return getPronunciation(word.getWord());
    }

    private String getPronunciation(WordObject word) {
        return getPronunciation(word.getWord());
    }*/

    private fun getDefinitions(word: WordOfTheDay): List<Definition>? {
        val items = word.definitions
        if (!DataUtil.isEmpty(items)) {
            val definitions = ArrayList<Definition>(items!!.size)
/*            for (item in items) {
                //definitions.add(new WordnikDefinition(item.getPartOfSpeech(), item.getText()));
            }*/
            return definitions
        }
        return null
    }

    private fun getDefinitions(items: List<Definition>?): List<Definition>? {
        if (!DataUtil.isEmpty(items)) {
            val definitions = ArrayList<Definition>(items!!.size)
            for (item in items) {
                definitions.add(Definition(partOfSpeech = item.partOfSpeech, text = item.text))
            }
            return definitions
        }
        return null
    }

    private fun getExamples(word: WordOfTheDay): List<String>? {
        val items = word.examples
        items?.run {
            val examples = ArrayList<String>(size)
            forEach { item ->
                examples.add(item)
            }
            return examples
        }
        return null;
    }

    private fun getExamples(items: List<Example>?): List<String>? {
        items?.run {
            val examples = ArrayList<String>(items.size)
            forEach {
                it.text?.run {
                    examples.add(this)
                }

            }
            return examples
        }
        return null
    }

    private fun getExamplesBy(definitions: List<Definition>?): List<String>? {
        if (!DataUtil.isEmpty(definitions)) {
            val examples = ArrayList<String>()
            for (def in definitions!!) {
                if (DataUtil.isEmpty(def.exampleUses))
                    continue
                def.exampleUses?.forEach {
                    it.text?.run {
                        examples.add(this)
                    }
                }
            }
            return examples
        }
        return null
    }

    private fun getSynonyms(relateds: List<Related>?): List<String>? {
        val related = getRelated(relateds, Constants.Word.SYNONYM)
        return related?.words?.toList()
    }

    private fun getAntonyms(relateds: List<Related>?): List<String>? {
        val related = getRelated(relateds, Constants.Word.ANTONYM)
        return related?.words?.toList()
    }


    private fun getPronunciation(word: String, limit: Int): String? {
        for (index in keys.indices) {
            val api = getWordApi()
            try {
                val sourceDictionary: String? = null
                val typeFormat: String? = null
                val useCanonical = "true"

                val result = api.getTextPronunciations(
                    word,
                    useCanonical,
                    sourceDictionary,
                    typeFormat,
                    limit
                )
                val pronunciations: List<TextPron> = result.toList()
                if (!DataUtil.isEmpty(pronunciations)) {
                    var pronunciation = pronunciations[0].raw
                    for (indexX in 1 until pronunciations.size) {
                        if (pronunciation!!.length > pronunciations[indexX].raw!!.length) {
                            pronunciation = pronunciations[indexX].raw
                        }
                    }
                    pronunciation = pronunciation!!.replace("(?s)<i>.*?</i>".toRegex(), "")
                    return pronunciation
                }
            } catch (error: Throwable) {
                Timber.e(error)
                iterateQueue()
            }

        }
        return null
    }

    private fun getDefinitions(word: String, limit: Int): List<Definition>? {
        var word = word
        var index = 0
        var notFound = false
        while (index < keys.size) {
            val api = getWordApi()
            try {
                val partOfSpeech: String? = null
                val includeRelated = "false"
                val sourceDictionaries: Array<String>? = null
                val useCanonical = "true"
                val includeTags = "false"
                val definitions = api.getDefinitions(
                    word,
                    limit,
                    partOfSpeech,
                    includeRelated,
                    sourceDictionaries,
                    useCanonical,
                    includeTags
                )
                return Arrays.asList(*definitions)
            } catch (error: Exception) {
                Timber.e(error)
                if (error is ClientException) {
                    if (error.toString().contains("404")) {
                        if (notFound) {
                            break
                        }
                        notFound = true
                        word = TextUtil.toTitleCase(word)
                        index--
                        continue
                    }
                }
                iterateQueue()
            }
        }
        return null
    }

    fun getExamples(word: String, limit: Int): List<String>? {
        for (index in keys.indices) {
            val api = getWordApi()
            try {
                val includeDuplicates = "true"
                val useCanonical = "true"
                val skip = 0
                val results = api.getExamples(word, includeDuplicates, useCanonical, skip, limit)
                return results.examples?.toList() //Arrays.asList(results.getExamples())
            } catch (e: Exception) {
                Timber.e(e)
                iterateQueue()
            }

        }
        return null
    }

    fun query(query: String, limit: Int): List<WordnikWord>? {

        val includePartOfSpeech: String? = null
        val excludePartOfSpeech: String? = null
        val caseSensitive = "false"
        val minCorpusCount = 5
        val maxCorpusCount = -1
        val minDictionaryCount = 1
        val maxDictionaryCount = -1
        val minLength = 1
        val maxLength = -1
        val skip = 0

        for (index in keys.indices) {
            val api = getWordsApi()

            /*            try {
                WordSearchResults results = api.searchWords(query, includePartOfSpeech, excludePartOfSpeech,
                        caseSensitive, minCorpusCount, maxCorpusCount, minDictionaryCount, maxDictionaryCount, minLength, maxLength, skip, limit
                );

                if (results != null) {
                    List<WordSearchResult> searches = results.getSearchResults();
                    if (!DataUtil.isEmpty(searches)) {
                        List<WordnikWord> words = new ArrayList<>(searches.size());
                        for (WordSearchResult result : searches) {
                            words.add(new WordnikWord(result.getWord().toLowerCase()));
                        }
                        return words;
                    }
                }
            } catch (Exception e) {
                Timber.e(e);
                iterateQueue();
            }*/
        }
        return null
    }

    private fun getRelateds(word: String, relationshipTypes: String, limit: Int): List<Related>? {
        var word = word
        var index = 0
        var notFound = false
        while (index++ < keys.size) {
            val api = getWordApi()
            try {
                val useCanonical = "true"

                val relateds = api.getRelatedWords(word, useCanonical, relationshipTypes, limit)
                return Arrays.asList(*relateds)

            } catch (error: Throwable) {
                Timber.e(error)
                if (error is ClientException) {
                    if (error.toString().contains("404")) {
                        if (notFound) {
                            break
                        }
                        notFound = true
                        word = TextUtil.toTitleCase(word)
                        index--
                        continue
                    }
                }
                iterateQueue()
            }
        }
        return null
    }


    private fun getRelated(relateds: List<Related>?, relationshipType: String): Related? {
        if (!DataUtil.isEmpty(relateds)) {
            for (related in relateds!!) {
                if (relationshipType == related.relationshipType) {
                    return related
                }
            }
        }
        return null
    }
}