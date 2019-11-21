package com.dreampany.framework.api.theme

import android.content.Context
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.DataUtil
import com.google.common.collect.Maps
import timber.log.Timber

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Created by roman on 2019-11-18
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ColorManager
@Inject constructor(
    private val context: Context
) {

    private val random: Random = Random(System.currentTimeMillis())
    private val colors: MutableMap<Type, MutableList<Int>>
    private val materialColors = mutableListOf<Int>()

    init {
        colors = Maps.newConcurrentMap()
        materialColors.addAll(
            listOf(
                0xffe57373,
                0xfff06292,
                0xffba68c8,
                0xff9575cd,
                0xff7986cb
                /*0xff64b5f6,
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
                0xff90a4ae*/
            ) as Collection<Int>
        )
    }

    @Synchronized
    fun getNextColor(type: Type): Int {
        if (!colors.containsKey(type)) {
            colors.put(type, mutableListOf())
        }
        val list: MutableList<Int> = colors.get(type)!!
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