package com.dreampany.common.theme

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
                0xffe57373,
                0xfff06292,
                0xffba68c8,
                0xff9575cd,
                0xff7986cb,
                0xff64b5f6,
                0xff4fc3f7,
                0xff4dd0e1,
                0xff4db6ac,
                0xff81c784,
                0xffaed581,
                0xffff8a65,
                0xffd4e157,
                0xffffd54f,
                0xffffb74d,
                0xffa1887f,
                0xff90a4ae
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