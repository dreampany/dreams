package com.dreampany.frame.ui

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.translation.data.model.TextTranslation
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
abstract class BaseItemKt<T, VH : BaseItemKt.ViewHolder, S : Serializable> : AbstractFlexibleItem<VH>, IFilterable<S>, Serializable {

    protected var item: T? = null
    @LayoutRes
    protected var layoutId: Int = 0
    protected var success: Boolean = false

    protected constructor(item: T?, @LayoutRes layoutId: Int) {
        this.item = item
        this.layoutId = layoutId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as BaseItemKt<T, VH, S>
        return Objects.equal(this.item, item)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item)
    }

/*    fun setItem(item: T) {
        this.item = item
    }

    fun setLayoutId(@LayoutRes layoutId: Int) {
        this.layoutId = layoutId
    }

    fun setSuccess(success: Boolean) {
        this.success = success
    }*/

    abstract class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(view, adapter) {
        fun getContext(): Context {
            return itemView.context
        }
    }


}