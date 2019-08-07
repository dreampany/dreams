package com.dreampany.frame.ui.model

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.DisplayUtil
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFilterable
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import java.io.Serializable

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseItem<T : Base, VH : BaseItem.ViewHolder, S : Serializable>(var item: T, @LayoutRes var layoutId: Int = 0) :
    AbstractFlexibleItem<VH>(), IFilterable<S>, Serializable {

    var success: Boolean = false
    var favorite: Boolean = false
    var notify: Boolean = false
    var alert: Boolean = false
    var time: Long = 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as BaseItem<T, VH, S>
        return Objects.equal(this.item, item.item)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item)
    }

    override fun getLayoutRes(): Int {
        return layoutId
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: VH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        holder.bind(position, this)
    }

    abstract class ViewHolder(val view: View, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(view, adapter) {

        open fun getContext(): Context {
            return view.context
        }

        open fun <T : Base> setTag(tag: T) {
            view.setTag(tag)
        }

        open fun <T : Base> getTag(): T? {
            return view.tag as T?
        }

        open fun getSpanHeight(spanCount: Int, itemOffset: Int): Int {
            return (DisplayUtil.getScreenWidthInPx(getContext()) / spanCount) - (DisplayUtil.dpToPixels(
                itemOffset.toFloat()
            ) * spanCount)
        }

        abstract fun <VH : ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        )

    }
}