package com.dreampany.framework.misc.structure

import java.io.Serializable

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

data class MutablePair<A, B>(
    var first: A,
    var second: B
) : Serializable {
    override fun toString(): String = "($first, $second)"
}

infix fun <A, B> A.to(that: B): MutablePair<A, B> = MutablePair(this, that)
fun <T> MutablePair<T, T>.toList(): List<T> = listOf(first, second)

data class MutableTriple<A, B, C>(
    var first: A,
    var second: B,
    var third: C
) : Serializable {
    override fun toString(): String = "($first, $second, $third)"
}

fun <T> MutableTriple<T, T, T>.toList(): List<T> = listOf(first, second, third)