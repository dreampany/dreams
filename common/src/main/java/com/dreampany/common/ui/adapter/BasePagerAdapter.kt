package com.dreampany.common.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dreampany.common.misc.constant.Constants

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BasePagerAdapter<T : Fragment>(val activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {

    protected val items: ArrayList<T>
    protected val titles: Map<T, Int>

    init {
        items = ArrayList()
        titles = HashMap()
    }

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment = items.get(position)

    open fun getTitle(position: Int): String {
        val item = items.get(position)
        val res = titles.get(item) ?: return Constants.Default.STRING
        return activity.getString(res)
    }

    fun addItem(item: T) {
        items.add(item)
    }

    fun getItem(position: Int): T? = items.get(position)
}