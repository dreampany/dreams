package com.dreampany.word.api.wordnik

import com.dreampany.word.api.wordnik.core.*
import com.dreampany.word.api.wordnik.model.*

/**
 * Created by roman on 2019-06-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

class WordApi(basePath: String = "https://api.wordnik.com/v4") : ApiClient(basePath) {

    /**
     * Fetches audio metadata for a word.
     * The metadata includes a time-expiring fileUrl which allows reading the audio file directly from the API.  Currently only audio pronunciations from the American Heritage Dictionary in mp3 format are supported.
     * @param word Word to get audio for.
     * @param useCanonical Use the canonical form of the word (optional, default to false)
     * @param limit Maximum number of results to return (optional, default to 50)
     * @return Array<AudioFile>
     */
    @Suppress("UNCHECKED_CAST")
    fun getAudio(word: String, useCanonical: String, limit: Int): Array<AudioFile> {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap =
            mapOf("useCanonical" to listOf(useCanonical), "limit" to listOf(limit.toString()))
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/audio",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Array<AudioFile>>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<AudioFile>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Return definitions for a word
     *
     * @param word Word to return definitions for
     * @param limit Maximum number of results to return (optional, default to 200)
     * @param partOfSpeech CSV list of part-of-speech types (optional)
     * @param includeRelated Return related words with definitions (optional, default to false)
     * @param sourceDictionaries Source dictionary to return definitions from.  If &#39;all&#39; is received, results are returned from all sources. If multiple values are received (e.g. &#39;century,wiktionary&#39;), results are returned from the first specified dictionary that has definitions. If left blank, results are returned from the first dictionary that has definitions. By default, dictionaries are searched in this order: ahd, wiktionary, webster, century, wordnet (optional)
     * @param useCanonical If true will try to return the correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @param includeTags Return a closed set of XML tags in response (optional, default to false)
     * @return Array<Definition>
     */
    @Suppress("UNCHECKED_CAST")
    fun getDefinitions(
        word: String,
        limit: Int,
        partOfSpeech: String?,
        includeRelated: String,
        sourceDictionaries: Array<String>?,
        useCanonical: String,
        includeTags: String
    ): Array<Definition> {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf(
            "limit" to listOf(limit.toString()),
            "includeRelated" to listOf(includeRelated),
            "useCanonical" to listOf(useCanonical),
            "includeTags" to listOf(includeTags),
            ApiKey to listOf(keyOfApi!!)
        )
        partOfSpeech?.let {
            localVariableQuery.plus("partOfSpeech" to listOf(partOfSpeech))
        }
        sourceDictionaries?.let {
            localVariableQuery.plus(
                "sourceDictionaries" to toMultiValue(
                    sourceDictionaries.toList(),
                    "csv"
                )
            )
        }
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/definitions",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Array<Definition>>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<Definition>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Fetches etymology data
     *
     * @param word Word to return
     * @param useCanonical If true will try to return the correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional)
     * @return Array<String>
     */
    @Suppress("UNCHECKED_CAST")
    fun getEtymologies(word: String, useCanonical: String): Array<String> {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf("useCanonical" to listOf(useCanonical))
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/etymologies",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Array<String>>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<String>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Returns examples for a word
     *
     * @param word Word to return examples for
     * @param includeDuplicates Show duplicate examples from different sources (optional, default to false)
     * @param useCanonical If true will try to return the correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @param skip Results to skip (optional, default to 0)
     * @param limit Maximum number of results to return (optional, default to 5)
     * @return ExampleSearchResults
     */
    @Suppress("UNCHECKED_CAST")
    fun getExamples(
        word: String,
        includeDuplicates: String,
        useCanonical: String,
        skip: Int,
        limit: Int
    ): ExampleSearchResults {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf(
            "includeDuplicates" to listOf(includeDuplicates),
            "useCanonical" to listOf(useCanonical),
            "skip" to listOf(skip.toString()),
            "limit" to listOf(limit.toString()),
            ApiKey to listOf(keyOfApi!!)
        )
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/examples",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<ExampleSearchResults>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ExampleSearchResults
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Returns syllable information for a word
     *
     * @param word Word to get syllables for
     * @param useCanonical If true will try to return a correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @param sourceDictionary Get from a single dictionary. Valid options: ahd, century, wiktionary, webster, and wordnet. (optional)
     * @param limit Maximum number of results to return (optional, default to 50)
     * @return Array<Syllable>
     */
    @Suppress("UNCHECKED_CAST")
    fun getHyphenation(
        word: String,
        useCanonical: String,
        sourceDictionary: String,
        limit: Int
    ): Array<Syllable> {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf(
            "useCanonical" to listOf(useCanonical),
            "sourceDictionary" to listOf(sourceDictionary),
            "limit" to listOf(limit.toString())
        )
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/hyphenation",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Array<Syllable>>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<Syllable>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Fetches bi-gram phrases for a word
     *
     * @param word Word to fetch phrases for
     * @param limit Maximum number of results to return (optional, default to 5)
     * @param wlmi Minimum WLMI for the phrase (optional, default to 0)
     * @param useCanonical If true will try to return the correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @return Array<Bigram>
     */
    @Suppress("UNCHECKED_CAST")
    fun getPhrases(word: String, limit: Int, wlmi: Int, useCanonical: String): Array<Bigram> {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf(
            "limit" to listOf(limit.toString()),
            "wlmi" to listOf(wlmi.toString()),
            "useCanonical" to listOf(useCanonical)
        )
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/phrases",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Array<Bigram>>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<Bigram>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Given a word as a string, returns relationships from the Word Graph
     *
     * @param word Word to fetch relationships for
     * @param useCanonical If true will try to return the correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @param relationshipTypes Limits the total results per type of relationship type (optional)
     * @param limitPerRelationshipType Restrict to the supplied relatinship types (optional, default to 10)
     * @return Array<Related>
     */
    @Suppress("UNCHECKED_CAST")
    fun getRelatedWords(
        word: String,
        useCanonical: String,
        relationshipTypes: String,
        limitPerRelationshipType: Int
    ): Array<Related> {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf(
            "useCanonical" to listOf(useCanonical),
            "relationshipTypes" to listOf(relationshipTypes),
            "limitPerRelationshipType" to listOf(limitPerRelationshipType.toString()),
            ApiKey to listOf(keyOfApi!!)
        )
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/relatedWords",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Array<Related>>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<Related>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Returns the Scrabble score for a word
     *
     * @param word Word to get scrabble score for.
     * @return Long
     */
    @Suppress("UNCHECKED_CAST")
    fun getScrabbleScore(word: String): Long {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf()
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/scrabbleScore",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Long>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Long
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Returns text pronunciations for a given word
     *
     * @param word Word to get pronunciations for
     * @param useCanonical If true will try to return a correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @param sourceDictionary Get from a single dictionary (optional)
     * @param typeFormat Text pronunciation type (optional)
     * @param limit Maximum number of results to return (optional, default to 50)
     * @return Array<TextPron>
     */
    @Suppress("UNCHECKED_CAST")
    fun getTextPronunciations(
        word: String,
        useCanonical: String,
        sourceDictionary: String?,
        typeFormat: String?,
        limit: Int
    ): Array<TextPron> {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf(
            "useCanonical" to listOf(useCanonical),
            "limit" to listOf(limit.toString()),
            ApiKey to listOf(keyOfApi!!)
        )
        sourceDictionary?.let {
            localVariableQuery.plus("sourceDictionary" to listOf(sourceDictionary))
        }
        typeFormat?.let {
            localVariableQuery.plus("typeFormat" to listOf(typeFormat))
        }
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/pronunciations",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Array<TextPron>>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<TextPron>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Returns a top example for a word
     *
     * @param word Word to fetch examples for
     * @param useCanonical If true will try to return the correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @return Example
     */
    @Suppress("UNCHECKED_CAST")
    fun getTopExample(word: String, useCanonical: String): Example {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf("useCanonical" to listOf(useCanonical))
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/topExample",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<Example>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Example
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Given a word as a string, returns the WordObject that represents it
     *
     * @param word String value of WordObject to return
     * @param useCanonical If true will try to return the correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @param includeSuggestions Return suggestions (for correct spelling, case variants, etc.) (optional, default to true)
     * @return WordObject
     */
    @Suppress("UNCHECKED_CAST")
    fun getWord(word: String, useCanonical: String, includeSuggestions: String): WordObject {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf(
            "useCanonical" to listOf(useCanonical),
            "includeSuggestions" to listOf(includeSuggestions),
            ApiKey to listOf(keyOfApi!!)
        )
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<WordObject>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as WordObject
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     * Returns word usage over time
     *
     * @param word Word to return
     * @param useCanonical If true will try to return the correct word root (&#39;cats&#39; -&gt; &#39;cat&#39;). If false returns exactly what was requested. (optional, default to false)
     * @param startYear Starting Year (optional, default to 1800)
     * @param endYear Ending Year (optional, default to 2012)
     * @return FrequencySummary
     */
    @Suppress("UNCHECKED_CAST")
    fun getWordFrequency(
        word: String,
        useCanonical: String,
        startYear: Int,
        endYear: Int
    ): FrequencySummary {
        val localVariableBody: Any? = null
        val localVariableQuery: MultiValueMap = mapOf(
            "useCanonical" to listOf(useCanonical),
            "startYear" to listOf(startYear.toString()),
            "endYear" to listOf(endYear.toString())
        )
        val localVariableHeaders: Map<String, String> = mapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/word.json/$word/frequency",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<FrequencySummary>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as FrequencySummary
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )
            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

}