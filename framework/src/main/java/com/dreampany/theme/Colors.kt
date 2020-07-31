package com.dreampany.theme

import com.google.common.collect.Maps
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Colors @Inject constructor() {

    private val random: Random
    private val colors: MutableMap<String, MutableList<Int>>
    private val materialColors = mutableListOf<Int>()

    init {
        random = Random(System.currentTimeMillis())
        colors = Maps.newConcurrentMap()
        materialColors.addAll(
            listOf(
                0xffcca700,
                0xffbb114a,
                0xff883696,
                0xff593696,
                0xff394893,
                0xff0b6fc1,
                0xff0988c3,
                0xff1e9eae,
                0xff3b9188,
                0xff3f8d43,
                0xff699933,
                0xffcc3000,
                0xff9fad1f,
                0xffcc9c00,
                0xffcc7a00,
                0xff755f57,
                0xff566b76
            ) as Collection<Int>
        )
    }


    @Synchronized
    fun nextColor(key: String): Int {
        if (!colors.containsKey(key)) {
            colors.put(key, mutableListOf())
        }
        val list: MutableList<Int> = colors.get(key)!!
        val bank = arrayListOf<Int>()
        materialColors.forEach {
            if (!list.contains(it)) {
                bank.add(it)
            }
        }
        val color: Int = if (bank.isEmpty()) materialColors.random(random) else bank.random(random)
        list.add(color)
        Timber.v("COLOR %s - %s %s", bank.size, list.size, color)
        return color
    }
}