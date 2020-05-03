package com.dreampany.framework.ui.adapter

import android.widget.Filter
import android.widget.Filterable
import com.google.common.base.Objects

/**
 * Created by roman on 29/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class SearchAdapter<T, VH : BaseAdapter.ViewHolder<T, VH>>(listener: Any? = null) :
    BaseAdapter<T, VH>(listener), Filterable {

    private var filters: MutableList<T>
    private var oldConstraint: CharSequence? = null
    private var constraint: CharSequence? = null

    init {
        filters = arrayListOf<T>()
    }

    protected abstract fun filter(item: T, constraint: CharSequence): Boolean

    override fun getItemCount(): Int = filters.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val temp : MutableList<T>
                if (constraint.isEmpty()) {
                    temp = items
                } else {
                    val result = items.filter { filter(it, constraint) }.toMutableList()
                    temp = result
                }
                val result = FilterResults()
                result.values = temp
                return result
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filters = results.values as MutableList<T>
                notifyDataSetChanged()
            }
        }
    }

    fun hasNewFilter(constraint: CharSequence): Boolean = !Objects.equal(oldConstraint, constraint)

    fun filters(constraint: CharSequence) {
        if (hasNewFilter(constraint)) {
            this.constraint = constraint
            filter.filter(this.constraint)
        }
    }
}