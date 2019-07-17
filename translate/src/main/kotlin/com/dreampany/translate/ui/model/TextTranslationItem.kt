package com.dreampany.translate.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.translation.data.model.TextTranslation
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TextTranslationItem private constructor(item: TextTranslation, @LayoutRes layoutId: Int = 0) :
    TranslationItem<TextTranslation, TextTranslationItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: TextTranslation): TranslationItem<*, *, *> {
            return TextTranslationItem(item, 0)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        TranslationItem.ViewHolder(view, adapter) {

        override fun <VH : TranslationItem.ViewHolder, T : BaseKt, S : Serializable, I : TranslationItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {

        }

    }
}