package com.dreampany.tools.api.wordnik.model

import com.dreampany.frame.util.DataUtil

/**
 * Created by roman on 2019-08-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class WordnikWord(
    var word: String? = null,
    var partOfSpeech: String? = null,
    var pronunciation: String? = null,
    var definitions: List<Definition>? = null,
    var examples: List<String>? = null,
    var synonyms: List<String>? = null,
    var antonyms: List<String>? = null
) {

    fun hasDefinition(): Boolean {
        return !DataUtil.isEmpty(definitions)
    }

    fun hasExample(): Boolean {
        return !DataUtil.isEmpty(examples)
    }

    fun hasSynonyms(): Boolean {
        return !DataUtil.isEmpty(synonyms)
    }

    fun hasAntonyms(): Boolean {
        return !DataUtil.isEmpty(antonyms)
    }
}