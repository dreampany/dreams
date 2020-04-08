package com.dreampany.adapter

import android.os.Handler
import android.os.Message
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.enums.Payload
import com.dreampany.adapter.item.IFilterable
import com.dreampany.adapter.item.IFlexible
import kotlinx.coroutines.Runnable
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

/**
 * Created by roman on 28/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FlexibleAdapter<VH : RecyclerView.ViewHolder, T : IFlexible<VH>> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int): Boolean
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View?, position: Int): Boolean
    }

    var clickListener: OnItemClickListener? = null
    var longClickListener: OnItemLongClickListener? = null

    private val executor: Executor
    private var filterTask: FilterTask? = null

    protected val UPDATE = 1
    protected val FILTER = 2
    protected val LOAD_MORE_COMPLETE = 8

    private var useDiffUtil = false

    private var items: ArrayList<T>? = null
    private var temps: ArrayList<T>? = null
    private var originals: ArrayList<T>? = null
    private var notices: ArrayList<T>? = null

    private var filterText: CharSequence? = null
        set(value) {
            field = value?.trim()
        }
    private var oldFilterText: CharSequence? = null

    private var endlessLoading = false
    private var filtering = false

    val hasFilter: Boolean
        get() = filterText.isNullOrEmpty().not()

    val isEmpty : Boolean
        get() = itemCount == 0

    init {
        executor = Executors.newSingleThreadExecutor()
    }

    override fun getItemCount(): Int {
        return items?.size.value()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    fun getRealItemCount() : Int = if (hasFilter) itemCount else itemCount

    fun hasNewFilter(filterText: CharSequence?): Boolean = oldFilterText.equals(filterText).not()

    fun getCurrentItems() : List<T> = Collections.unmodifiableList(items)

    fun getGlobalPositionOf(item: T) : Int = items?.indexOf(item) ?: -1
    fun getCardinalPositionOf(item: T) : Int {
        var position = getGlobalPositionOf(item)
        //TODO reduce scrollableHeaders size
        return position
    }


    protected fun filterObject(item: T, filterText: CharSequence?): Boolean {
        return item is IFilterable && item.filter(filterText)
    }

    @CallSuper
    protected fun onPostFilter() {

    }

    private fun filterObject(item: T, values: ArrayList<T>): Boolean {
        if (filterTask?.running.value().not()) return false
        if (originals != null && values.contains(item)) return false

        val filteredItems = ArrayList<T>()
        filteredItems.add(item)

        val filtered = filterObject(item, filterText)
        if (filtered) {
            //TODO add header
            values.addAll(filteredItems)
        }
        item.setHidden(filtered.not())
        return filtered
    }

    @Synchronized
    private fun filter(@NonNull unfilteredItems: ArrayList<T>) {
        var filteredItems = ArrayList<T>()
        filtering = true

        if (hasFilter && hasNewFilter(filterText)) {
            for (item in unfilteredItems) {
                if (filterTask?.running.value().not()) return
                filterObject(item, filteredItems)
            }
        } else if (hasNewFilter(filterText)) {
            filteredItems = unfilteredItems
            originals = null
        }

        if (hasNewFilter(filterText)) {
            oldFilterText = filterText
        }
        filtering = false
    }

    @Synchronized
    private fun animateDiff(@Nullable newItems: ArrayList<T>?, payloadChange: Payload) {
        if (useDiffUtil) {

        } else {

        }
    }

    @Synchronized
    private fun animateTo(@Nullable newItems: ArrayList<T>?, payloadChange: Payload) {
        notices = ArrayList<T>()

    }

    @Synchronized
    private fun executeNotices(payloadChange: Payload) {

    }

    inner class FilterTask(val what: Int, @Nullable newItems: ArrayList<T>?) : Runnable {

        var running: Boolean = true
        private val newItems: ArrayList<T>

        init {
            this.newItems = if (newItems == null) ArrayList() else ArrayList(newItems)
        }

        override fun run() {
            if (endlessLoading) {
                return
            }
            //TODO RestoreItem
            when (what) {
                UPDATE -> {

                }
                FILTER -> {

                }
            }
        }
    }

    inner class HandlerCallback : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                UPDATE,
                FILTER -> {
                    filterTask?.running = false
                    filterTask = FilterTask(msg.what, msg.obj as ArrayList<T>?)
                    executor.execute(filterTask)
                    return true
                }
                LOAD_MORE_COMPLETE -> {
                    //TODO hide progress item
                    return true
                }
            }
            return false
        }

    }

    inner class DiffUtilCallback : DiffUtil.Callback() {

        protected lateinit var oldItems: ArrayList<T>
            get
            set
        protected lateinit var newItems: ArrayList<T>
            set

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems.get(oldItemPosition)
            val newItem = newItems.get(newItemPosition)
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems.get(oldItemPosition)
            val newItem = newItems.get(newItemPosition)
            return !oldItem.shouldNotifyChange(newItem)
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
            Payload.CHANGE

    }
}