package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.data.model.Load
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by Roman-372 on 8/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoadItem
private constructor(
    item: Load,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem< LoadItem.ViewHolder,Load, String>(item, layoutId) {

    companion object {
        fun getItem(item: Load): LoadItem {
            return LoadItem(item)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as LoadItem
        return Objects.equal(this.item, item.item)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): LoadItem.ViewHolder {
        return LoadItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String?): Boolean {
        return false
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH,T,  S>> bind(
            position: Int,
            item: I
        ) {

        }
    }
}