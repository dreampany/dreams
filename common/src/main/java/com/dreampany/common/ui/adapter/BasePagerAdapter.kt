package com.dreampany.common.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BasePagerAdapter<T : Fragment>(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {

    protected val items: ArrayList<T>

    init {
        items = ArrayList()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: T) {
        items.add(item)
    }
}