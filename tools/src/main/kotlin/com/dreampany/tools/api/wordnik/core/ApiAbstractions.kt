package com.dreampany.tools.api.wordnik.core

/**
 * Created by roman on 2019-06-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

typealias MultiValueMap = Map<String, List<String>>

fun collectionDelimiter(collectionFormat: String) = when (collectionFormat) {
    "csv" -> ","
    "tsv" -> "\t"
    "pipes" -> "|"
    "ssv" -> " "
    else -> ""
}

val defaultMultiValueConverter: (item: Any?) -> String = { item -> "$item" }

fun <T : Any?> toMultiValue(items: List<T>, collectionFormat: String, map: (item: Any?) -> String = defaultMultiValueConverter): List<String> {
    return when (collectionFormat) {
        "multi" -> items.map(map)
        else -> listOf(items.joinToString(separator = collectionDelimiter(collectionFormat), transform = map))
    }
}