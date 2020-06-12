package com.dreampany.framework.ui.model

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.dreampany.adapter.FlexibleAdapter
import com.dreampany.adapter.holder.FlexibleViewHolder
import com.dreampany.adapter.item.FlexibleItem
import com.dreampany.adapter.item.IFlexible
import com.dreampany.framework.misc.exts.dpToPx
import com.dreampany.framework.misc.exts.screenWidth
import com.google.common.base.Objects

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseItem<VH : BaseItem.ViewHolder<T>, T>(
    @LayoutRes var layoutId: Int = 0,
    var item: T,
    var success: Boolean = false,
    var favorite: Boolean = false,
    var notify: Boolean = false,
    var alert: Boolean = false,
    var time: Long = 0L
) : FlexibleItem<VH>() {

    override fun hashCode(): Int {
        return Objects.hashCode(item)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as BaseItem<VH, T>
        return Objects.equal(this.item, item.item)
    }

    override fun getLayoutRes(): Int = layoutId

    override fun <T : IFlexible<VH>> bindViewHolder(
        adapter: FlexibleAdapter<VH, T>,
        holder: VH,
        position: Int,
        payloads: List<Any>
    ) {
        holder.bind(position, this)
    }

    abstract class ViewHolder<T>(
        val view: View,
        adapter: FlexibleAdapter<ViewHolder<T>, BaseItem<ViewHolder<T>, T>>
    ) : FlexibleViewHolder<ViewHolder<T>, BaseItem<ViewHolder<T>, T>>(view, adapter) {
        protected val context: Context
            get() = view.context

        protected fun <T> setTag(tag: T?) {
            view.setTag(tag)
        }

        protected fun <T> getTag(): T? = view.tag as T?

        protected fun spanHeight(spans: Int, offset: Int): Int =
            (context.screenWidth / spans) - (context.dpToPx(offset.toFloat()) * spans)

        abstract fun <VH : ViewHolder<T>, T, I : BaseItem<VH, T>> bind(
            position: Int,
            item: I
        )
    }
}