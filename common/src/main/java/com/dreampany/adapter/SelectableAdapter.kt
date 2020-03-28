package com.dreampany.adapter

import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.enums.Payload
import com.dreampany.adapter.holder.FlexibleViewHolder
import com.dreampany.ui.FlexibleLayoutManager
import com.dreampany.ui.IFlexibleLayoutManager
import com.dreampany.ui.Mode
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by roman on 29/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class SelectableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val selectedPositions: MutableSet<Int>
    private val boundViewHolders: MutableSet<FlexibleViewHolder>

    @Mode
    var mode: Int = Mode.IDLE
        get() = field
        set(value) {
            if (field == Mode.SINGLE && mode == Mode.IDLE) {
                //clearSelection()
                // TODO mode
            }
            field = value
            // TODO last item in action mode
        }

    protected var recycler: RecyclerView? = null
        get() = field

    private var layoutManager: IFlexibleLayoutManager? = null
        get() {
            if (field == null) {
                val layout = recycler?.layoutManager
                if (layout is IFlexibleLayoutManager) {
                    field = layout
                } else if (layout != null) {
                    field = FlexibleLayoutManager(recycler)
                }
            }
            return field
        }

    init {
        selectedPositions = Collections.synchronizedSet(TreeSet())
        boundViewHolders = HashSet()
    }

    abstract fun isSelectable(position: Int): Boolean

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recycler = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recycler = null
        layoutManager = null
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (holder is FlexibleViewHolder) {
            val contentView = holder.getContentView()
            contentView.isActivated = isSelected(position)
            if (contentView.isActivated &&)
        }
    }

    fun setLayoutManager(layoutManager: IFlexibleLayoutManager) {
        this.layoutManager = layoutManager
    }

    fun isSelected(position: Int): Boolean = selectedPositions.contains(position)

    fun toggleSelection(position: Int) {
        if (position < 0) return
        if (mode == Mode.SINGLE) {
            //TODO clearSelections()
        }
        if (isSelected(position)) {
            removeSelection(position)
        } else {
            addSelection(position)
        }
    }

    fun addSelection(position: Int): Boolean =
        isSelectable(position) && selectedPositions.add(position)

    fun removeSelection(position: Int): Boolean = selectedPositions.remove(position)

    fun clearSelection() {
        synchronized(selectedPositions) {
            val iterator: MutableIterator<Int> = selectedPositions.iterator()
            var positionStart = 0
            var itemCount = 0
            while (iterator.hasNext()) {
                val position = iterator.next()
                iterator.remove()
                if (positionStart + itemCount == position) {
                    itemCount++
                } else {
                    positionStart = position
                    itemCount = 1
                }
            }
        }
    }

    protected fun swapSelection(fromPosition: Int, toPosition: Int) {
        if (isSelected(fromPosition) && !isSelected(toPosition)) {
            removeSelection(fromPosition)
            addSelection(toPosition)
        } else if (!isSelected(fromPosition) && isSelected(toPosition)) {
            removeSelection(toPosition)
            addSelection(fromPosition)
        }
    }

    private fun notifySelectionChanged(positionStart: Int, itemCount: Int) {
        if (itemCount > 0) {
            if (boundViewHolders.isEmpty()) {
                notifyItemRangeChanged(
                    positionStart,
                    itemCount,
                    Payload.SELECTION
                )
            }
        }
    }
}

