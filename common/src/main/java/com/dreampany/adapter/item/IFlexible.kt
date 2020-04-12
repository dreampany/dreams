package com.dreampany.adapter.item

import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.FlexibleAdapter

/**
 * Created by roman on 28/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface IFlexible<VH : RecyclerView.ViewHolder> {
    fun isEnabled(): Boolean
    fun setEnabled(enabled: Boolean)
    fun isHidden(): Boolean
    fun setHidden(hidden: Boolean)

    @IntRange(from = 1)
    fun getSpanSize(spanCount: Int, position: Int): Int

    fun shouldNotifyChange(newItem: IFlexible<VH>): Boolean

    fun isSelectable(): Boolean

    fun setSelectable(selectable: Boolean)

    fun getBubbleText(position: Int): String

    fun isDraggable(): Boolean

    fun setDraggable(draggable: Boolean)

    fun isSwipeable(): Boolean

    fun setSwipeable(swipeable: Boolean)

    fun getItemViewType(): Int

    @LayoutRes
    fun getLayoutRes(): Int

    fun <V : ViewDataBinding, T : IFlexible<VH>> createViewHolder(
        binding: V,
        adapter: FlexibleAdapter<VH, T>
    ): VH

    fun <T : IFlexible<VH>> bindViewHolder(
        adapter: FlexibleAdapter<VH, T>,
        holder: VH,
        position: Int,
        payloads: List<Any>
    )

    fun unbindViewHolder(
        adapter: FlexibleAdapter<VH, IFlexible<VH>>,
        holder: VH,
        position: Int
    )

    fun onViewAttached(
        adapter: FlexibleAdapter<VH, IFlexible<VH>>,
        holder: VH,
        position: Int
    )

    fun onViewDetached(
        adapter: FlexibleAdapter<VH, IFlexible<VH>>,
        holder: VH,
        position: Int
    )
}