package com.dreampany.adapter.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.ui.FlexibleAdapter

/**
 * Created by roman on 28/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class FlexibleItem<VH : RecyclerView.ViewHolder> :
    IFlexible<VH> {

    protected var mEnabled = true
    protected var mHidden: Boolean = false
    protected var mSelectable = true
    protected var mDraggable: Boolean = true
    protected var mSwipeable: Boolean = true

    override fun isEnabled(): Boolean {
        return mEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        mEnabled = enabled
    }

    override fun isHidden(): Boolean {
        return mHidden
    }

    override fun setHidden(hidden: Boolean) {
        mHidden = hidden
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 1
    }

    override fun shouldNotifyChange(newItem: IFlexible<VH>): Boolean {
        return true
    }

    /*--------------------*/ /* SELECTABLE METHODS */ /*--------------------*/
    override fun isSelectable(): Boolean {
        return mSelectable
    }

    override fun setSelectable(selectable: Boolean) {
        mSelectable = selectable
    }

    override fun getBubbleText(position: Int): String {
        return (position + 1).toString()
    }

    /*-------------------*/ /* TOUCHABLE METHODS */ /*-------------------*/
    override fun isDraggable(): Boolean {
        return mDraggable
    }

    override fun setDraggable(draggable: Boolean) {
        mDraggable = draggable
    }

    override fun isSwipeable(): Boolean {
        return mSwipeable
    }

    override fun setSwipeable(swipeable: Boolean) {
        mSwipeable = swipeable
    }

    /*---------------------*/ /* VIEW HOLDER METHODS */ /*---------------------*/
    override fun getItemViewType(): Int {
        return getLayoutRes()
    }

    abstract override fun getLayoutRes(): Int

    override abstract fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<VH>>): VH

    override abstract fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<VH>>,
        holder: VH,
        position: Int,
        payloads: List<Any>
    )

    override fun unbindViewHolder(
        adapter: FlexibleAdapter<IFlexible<VH>>,
        holder: VH,
        position: Int
    ) {

    }

    override fun onViewAttached(
        adapter: FlexibleAdapter<IFlexible<VH>>,
        holder: VH,
        position: Int
    ) {
    }

    override fun onViewDetached(
        adapter: FlexibleAdapter<IFlexible<VH>>,
        holder: VH,
        position: Int
    ) {

    }
}