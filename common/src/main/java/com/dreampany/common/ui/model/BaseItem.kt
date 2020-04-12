package com.dreampany.common.ui.model

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.dreampany.adapter.FlexibleAdapter
import com.dreampany.adapter.holder.FlexibleViewHolder
import com.dreampany.adapter.item.IFlexible
import com.google.common.base.Objects

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseItem<VH : BaseItem.ViewHolder<T>, T>(
    @LayoutRes var layoutId: Int = 0,
    var item : T,
    var success: Boolean = false,
    var favorite: Boolean = false,
    var notify: Boolean = false,
    var alert: Boolean = false,
    var time: Long = 0L
) : IFlexible<VH> {

    override fun hashCode(): Int {
        return Objects.hashCode(item)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as BaseItem<VH, T>
        return Objects.equal(this.item, item.item)
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

        abstract fun <VH : ViewHolder<T>, T, I : BaseItem<VH, T>> bind(
            position: Int,
            item: I
        )
    }
}