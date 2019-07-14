package com.dreampany.translate.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.ui.model.BaseItemKt
import com.dreampany.translate.ui.adapter.TranslationAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class TranslationItem<T : BaseKt, VH : TranslationItem.ViewHolder, S : Serializable>(item: T?, @LayoutRes layoutId: Int = 0) :
    BaseItemKt<T, VH, S>(item, layoutId) {

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: VH,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bind(position, this)
    }

    abstract class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItemKt.ViewHolder(view, adapter) {

        val adapter: TranslationAdapter

        init {
            this.adapter = adapter as TranslationAdapter
        }

        abstract fun <VH : ViewHolder, T : BaseKt, S : Serializable, I : TranslationItem<T, VH, S>> bind(position: Int, item: I)
    }
}