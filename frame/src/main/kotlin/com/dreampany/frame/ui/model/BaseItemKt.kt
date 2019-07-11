package com.dreampany.frame.ui.model

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.dreampany.frame.data.model.BaseKt
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFilterable
import eu.davidea.viewholders.FlexibleViewHolder
import java.io.Serializable

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseItemKt<T : BaseKt, VH : BaseItemKt.ViewHolder, S : Serializable>(var item: T?, @LayoutRes var layoutId: Int = 0) :
    AbstractFlexibleItem<VH>(), IFilterable<S>, Serializable {

    protected var success: Boolean = false
    protected var favorite: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as BaseItemKt<T, VH, S>
        return Objects.equal(this.item, item)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item)
    }

    abstract class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(view, adapter) {
        internal fun getContext(): Context {
            return itemView.context
        }
    }

/*    abstract class ViewHolder<VH : RecyclerView.ViewHolder, T : IFlexible<VH>>(
        view: View,
        adapter: FlexibleAdapter<T>
    ) :
        FlexibleViewHolder(view, adapter) {
        internal fun getContext(): Context {
            return itemView.context
        }
    }*/
}