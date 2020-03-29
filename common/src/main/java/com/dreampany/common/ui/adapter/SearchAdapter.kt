package com.dreampany.common.ui.adapter

import android.widget.Filter
import android.widget.Filterable

/**
 * Created by roman on 29/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class SearchAdapter<T, VH : BaseAdapter.ViewHolder<T, VH>>(listener: Any? = null) :
    BaseAdapter<T, VH>(listener), Filterable {

    private var filters: MutableList<T>

    init {
        filters = arrayListOf<T>()
    }

    protected abstract fun filters(constraint: CharSequence): Boolean

    override fun getItemCount(): Int = filters.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                if (constraint.isEmpty()) {
                    filters = items
                } else {
                    val result = items.filter { filters(constraint) }.toMutableList()
                    filters = result
                }
                val result = FilterResults()
                result.values = filters
                return result
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filters = results.values as MutableList<T>
                notifyDataSetChanged()
            }
        }
    }
}