package com.dreampany.framework.ui.adapter

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dreampany.framework.misc.constant.Constants

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BasePagerFragmentAdapter<T : Fragment>(val fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    protected val items: ArrayList<T>
    protected val titles: MutableMap<T, String>

    init {
        items = arrayListOf()
        titles = mutableMapOf()
    }

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment = items.get(position)

    open fun clear() {
        items.clear()
        titles.clear()
        notifyDataSetChanged()
    }

    open fun getPosition(item: T): Int = items.indexOf(item)

    open fun getTitle(position: Int): String {
        val item = items.get(position)
        val title = titles.get(item) ?: return Constants.Default.STRING
        return title
    }

    open fun addItem(item: T, notify: Boolean = false) = addItem(item, 0, notify)

    open fun addItem(item: T, @StringRes titleRes: Int, notify: Boolean = false) {
        if (titleRes != 0) {
            titles.put(item, fragment.getString(titleRes))
        }
        val position = getPosition(item)
        if (position == -1) {
            items.add(item)
            if (notify)
                notifyItemInserted(itemCount - 1)
        } else {
            items[position] = item
            if (notify)
                notifyItemChanged(position)
        }
    }

    open fun addItem(item: T, title: String, notify: Boolean = false) {
        titles.put(item, title)
        val position = getPosition(item)
        if (position == -1) {
            items.add(item)
            if (notify)
                notifyItemInserted(itemCount - 1)
        } else {
            items[position] = item
            if (notify)
                notifyItemChanged(position)
        }
    }

    fun getItem(position: Int): T? = items.get(position)

    val isEmpty : Boolean
        get() = itemCount == 0
}