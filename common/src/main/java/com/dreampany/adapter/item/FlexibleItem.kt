package com.dreampany.adapter.item

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.FlexibleAdapter


/**
 * Created by roman on 28/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class FlexibleItem<VH : RecyclerView.ViewHolder>(
    private var enabled: Boolean = true,
    private var hidden: Boolean = true,
    private var selectable: Boolean = true,
    private var draggable: Boolean = true,
    private var swipeable: Boolean = true
) : IFlexible<VH> {

    override fun isEnabled(): Boolean = enabled

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    override fun isHidden(): Boolean = hidden

    override fun setHidden(hidden: Boolean) {
        this.hidden = hidden
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 1
    }

    override fun shouldNotifyChange(newItem: IFlexible<VH>): Boolean {
        return true
    }

    override fun isSelectable(): Boolean = selectable

    override fun setSelectable(selectable: Boolean) {
        this.selectable = selectable
    }

    override fun getBubbleText(position: Int): String {
        return (position + 1).toString()
    }

    override fun isDraggable(): Boolean = draggable

    override fun setDraggable(draggable: Boolean) {
        this.draggable = draggable
    }

    override fun isSwipeable(): Boolean = swipeable

    override fun setSwipeable(swipeable: Boolean) {
        this.swipeable = swipeable
    }

    override fun getItemViewType(): Int = getLayoutRes()

    abstract override fun getLayoutRes(): Int

    override abstract fun <V : ViewDataBinding, T : IFlexible<VH>> createViewHolder(
        binding: V,
        adapter: FlexibleAdapter<VH, T>
    ): VH

    override abstract fun <T : IFlexible<VH>> bindViewHolder(
        adapter: FlexibleAdapter<VH, T>,
        holder: VH,
        position: Int,
        payloads: List<Any>
    )

    override fun unbindViewHolder(
        adapter: FlexibleAdapter<VH, IFlexible<VH>>,
        holder: VH,
        position: Int
    ) {

    }

    override fun onViewAttached(
        adapter: FlexibleAdapter<VH, IFlexible<VH>>,
        holder: VH,
        position: Int
    ) {
    }

    override fun onViewDetached(
        adapter: FlexibleAdapter<VH, IFlexible<VH>>,
        holder: VH,
        position: Int
    ) {

    }
}