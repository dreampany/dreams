package com.dreampany.framework.ui.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dreampany.framework.misc.constant.Constant

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BasePagerAdapter<T : Fragment> : FragmentStateAdapter {

    protected val context: Context
    protected val items: ArrayList<T>
    protected val titles: MutableMap<T, String>
    protected val pageIds : ArrayList<Long>

    init {
        items = arrayListOf()
        titles = mutableMapOf()
        pageIds = arrayListOf()
    }

    constructor(fragment: Fragment) : super(fragment) {
        this.context = fragment.requireContext()
    }

    constructor(activity: AppCompatActivity) : super(activity) {
        this.context = activity
    }

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment = items.get(position)

    override fun getItemId(position: Int): Long = items.get(position).hashCode().toLong()

    override fun containsItem(itemId: Long): Boolean = pageIds.contains(itemId)

    open fun getPosition(item: T): Int = items.indexOf(item)

    open fun getTitle(position: Int): String {
        val item = items.get(position)
        val title = titles.get(item) ?: return Constant.Default.STRING
        return title
    }

    open fun addItem(item: T, notify: Boolean = false) = addItem(item, 0, notify)

    open fun addItem(item: T, @StringRes titleRes: Int, notify: Boolean = false) {
        if (titleRes != 0) {
            titles.put(item, context.getString(titleRes))
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

    open fun removeAt(position: Int): T {
        val item = items.removeAt(position)
        pageIds.remove(item.hashCode().toLong())
        titles.remove(item)
        notifyItemRangeChanged(position, items.size)
        notifyDataSetChanged()
        //notifyItemRemoved(position)
        //notifyItemRangeChanged(position, items.size)
        return item
    }

    open fun remove(item: T): Int {
        val position = getPosition(item)
        if (position >= 0) {
            removeAt(position)
        }
        return position
    }

    open fun clear() {
        items.clear()
        titles.clear()
        notifyDataSetChanged()
    }

    val isEmpty: Boolean
        get() = itemCount == 0
}