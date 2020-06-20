package com.dreampany.network.misc

import com.google.common.collect.BiMap

/**
 * Created by roman on 19/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
const val STRING_EMPTY = ""
val currentMillis: Long get() = System.currentTimeMillis()
val String?.value : String get() = STRING_EMPTY
fun Long.isExpired(delay: Long): Boolean = currentMillis - this > delay
fun MutableMap<String, Boolean>.valueOf(key: String): Boolean = get(key) ?: false
fun MutableMap<String, Int>.valueOf(key: String): Int = get(key) ?: 0
fun MutableMap<String, Long>.valueOf(key: String): Long = get(key) ?: 0L
fun MutableMap<String, Long>.timeOf(key: String): Long = get(key) ?: currentMillis
fun BiMap<Long, String>.valueOf(key: Long): String = get(key) ?: STRING_EMPTY
fun BiMap<Long, String>.inverseOf(key: String): Long = inverse().get(key) ?: 0L
