package com.dreampany.adapter

import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.enums.Payload
import com.dreampany.adapter.item.IFilterable
import com.dreampany.adapter.item.IFlexible
import kotlinx.coroutines.Runnable
import timber.log.Timber
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by roman on 28/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
open class FlexibleAdapter<VH : RecyclerView.ViewHolder, T : IFlexible<VH>>(
    @Nullable listeners: Any?
) : SelectableAdapter<VH>() {

    /* listeners */
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

    /* view types*/
    private lateinit var inflater: LayoutInflater
    private val types: HashMap<Int, T>

    private var filterText: CharSequence? = null
        set(value) {
            field = value?.trim()
        }
    private var oldFilterText: CharSequence? = null

    private var autoMap = false
    private var endlessLoading = false
    private var filtering = false


    val hasFilter: Boolean
        get() = filterText.isNullOrEmpty().not()

    val isEmpty: Boolean
        get() = itemCount == 0

    init {
        executor = Executors.newSingleThreadExecutor()
        types = HashMap()
    }

    override fun getItemCount(): Int {
        return items?.size.value
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item == null) {
            Timber.v("Item for ViewType not found! position=%s, items=%s", position, itemCount)
            return 0
        }
        keepType(item)
        autoMap = true
        return item.getItemViewType()
    }

    @Throws
    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): VH {
        val item = getTypeItem(viewType)
        if (item == null || !autoMap) {
            throw IllegalStateException(
                String.format(
                    "ViewType instance not found for viewType %s. You should implement the AutoMap properly.",
                    viewType
                )
            )
        }

        if (::inflater.isInitialized.not()) {
            inflater = LayoutInflater.from(parent.context)
        }
        return item.createViewHolder(
            DataBindingUtil.inflate(inflater, item.getLayoutRes(), parent, false),
            this
        )
    }

    @Throws
    override fun onBindViewHolder(holder: VH, position: Int) {
        this.onBindViewHolder(holder, position, Collections.unmodifiableList(ArrayList()))
    }

    @Throws
    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        payloads: List<Any>
    ) {
        check(autoMap) {
            // If everything has been set properly, this should never happen ;-)
            "AutoMap is not active, this method cannot be called. You should implement the AutoMap properly."
        }
        // Bind view activation with current selection
        super.onBindViewHolder(holder, position, payloads)
        // Bind the item
        val item = getItem(position)
        if (item != null) {
            holder.itemView.setEnabled(item.isEnabled())
            //item.bindViewHolder(this, holder, position, payloads)
        }
    }

    override fun isSelectable(position: Int): Boolean {
        TODO("Not yet implemented")
    }

    fun getRealItemCount(): Int = if (hasFilter) itemCount else itemCount

    fun hasNewFilter(filterText: CharSequence?): Boolean = oldFilterText.equals(filterText).not()

    @Nullable
    fun getItem(position: Int): T? {
        if (position < 0 || position >= itemCount) return null
        return items?.get(position)
    }

    fun getCurrentItems(): List<T> = Collections.unmodifiableList(items)

    fun getGlobalPositionOf(item: T): Int = items?.indexOf(item) ?: -1

    fun getCardinalPositionOf(item: T): Int {
        var position = getGlobalPositionOf(item)
        //TODO reduce scrollableHeaders size
        return position
    }

    fun getSameTypePositionOf(item: T): Int {
        var position = -1
        items?.forEach {
            if (it.getItemViewType() == item.getItemViewType()) {
                position++
                if (it.equals(item)) {
                    return@forEach
                }
            }
        }
        return position
    }

    fun contains(@Nullable item: T?): Boolean = items?.contains(item) ?: false

    fun calculatePositionFor(@Nullable item: T?, @Nullable comparator: Comparator<T>?): Int {
        if (items == null || item == null || comparator == null) return 0
        val sorted: ArrayList<T> = arrayListOf()
        items?.let {
            sorted.addAll(it)
        }
        if (!sorted.contains(item)) {
            sorted.add(item)
        }
        Collections.sort(sorted, comparator)
        return Math.max(0, sorted.indexOf(item))
    }

    fun insertItem(@NonNull item: T): Boolean =
        insertItem(itemCount, item)

    fun insertItem(@NonNull item: T, comparator: Comparator<T>): Boolean =
        insertItem(calculatePositionFor(item, comparator), item)

    fun insertItem(@IntRange(from = 0) position: Int, @NonNull item: T): Boolean =
        insertItems(position, listOf(item))

    fun insertItems(@IntRange(from = 0) position: Int, @NonNull items: List<T>): Boolean {
        if (items.isEmpty()) return false
        val initialCount = getRealItemCount()
        var position = position
        if (position < 0) {
            position = initialCount
        }

        performInsert(position, items, true)

        return true
    }

    fun updateItem(@NonNull item: T) = updateItem(item, null)

    fun updateItem(@NonNull item: T, @Nullable payload: Any?) =
        updateItem(getGlobalPositionOf(item), item, payload)

    fun updateItem(@IntRange(from = 0) position: Int, @NonNull item: T, @Nullable payload: Any?) {
        val itemCount = itemCount
        if (position < 0 || position >= itemCount) return
        items?.set(position, item)
        notifyItemChanged(position, payload)
    }

    fun addItem(@NonNull item: T): Boolean {
        if (contains(item)) {
            updateItem(item)
            return true
        } else {
            return insertItem(item)
        }
    }

    fun addItem(@NonNull item: T, comparator: Comparator<T>): Boolean {
        if (contains(item)) {
            updateItem(item)
            return true
        } else {
            return insertItem(calculatePositionFor(item, comparator), item)
        }
    }

    protected fun filterObject(item: T, filterText: CharSequence?): Boolean {
        return item is IFilterable && item.filter(filterText)
    }

    @CallSuper
    protected fun onPostFilter() {

    }

    private fun performInsert(position: Int, items: List<T>, notify: Boolean) {
        var position = position
        val itemCount = itemCount
        if (position < itemCount) {
            this.items?.addAll(position, items)
        } else {
            this.items?.addAll(items)
            position = itemCount
        }
        if (notify)
            notifyItemRangeInserted(position, items.size)
    }

    private fun getTypeItem(viewType: Int): T? = types.get(viewType)

    private fun keepType(item: T) {
        if (!types.containsKey(item.getItemViewType())) {
            types.put(item.getItemViewType(), item)
        }
    }

    private fun filterObject(item: T, values: ArrayList<T>): Boolean {
        if (filterTask?.running.value.not()) return false
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
                if (filterTask?.running.value.not()) return
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