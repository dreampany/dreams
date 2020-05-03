package com.dreampany.framework.ui.model

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.misc.extensions.toColor
import com.dreampany.framework.util.DisplayUtil
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
abstract class BaseItem<VH : BaseItem.ViewHolder, T : Base, S : Serializable>(
    var item: T,
    @LayoutRes var layoutId: Int = Constants.Default.INT
) : AbstractFlexibleItem<VH>(), IFilterable<S>, Serializable {

    var success: Boolean = false
    var favorite: Boolean = false
    var notify: Boolean = false
    var alert: Boolean = false
    var time: Long = 0L

    override fun hashCode(): Int {
        return Objects.hashCode(item)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as BaseItem<VH, T, S>
        return Objects.equal(this.item, item.item)
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

    abstract class ViewHolder(
        val view: View,
        adapter: FlexibleAdapter<*>,
        stickyHeader: Boolean = Constants.Default.BOOLEAN
    ) : FlexibleViewHolder(view, adapter, stickyHeader) {

       protected val context : Context
            get() = view.context

        open fun <T : Base> setTag(tag: T?) {
            view.setTag(tag)
        }

        open fun <T : Base> getTag(): T? {
            return view.tag as T?
        }

        open fun getSpanHeight(spanCount: Int, itemOffset: Int): Int {
            return (DisplayUtil.getScreenWidthInPx(context) / spanCount) - (DisplayUtil.dpToPixels(
                itemOffset.toFloat()
            ) * spanCount)
        }

        open fun getString(@StringRes resId:Int): String {
            return context.getString(resId)
        }

        open fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
            return context.getString(resId, formatArgs)
        }

        protected fun getFormattedString(@StringRes formatRes: Int, vararg values: Any): String {
            return String.format(getString(formatRes), *values)
        }

        open fun getColor(@ColorRes resId: Int): Int {
            return resId.toColor(context)
        }

        abstract fun <VH : ViewHolder, T : Base, S : Serializable , I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        )

    }
}