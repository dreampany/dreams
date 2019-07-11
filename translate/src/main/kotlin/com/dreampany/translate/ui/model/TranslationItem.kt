package com.dreampany.translate.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.ui.model.BaseItemKt
import com.dreampany.translate.ui.adapter.TranslationAdapter
import com.dreampany.translation.data.model.Translation
import eu.davidea.flexibleadapter.FlexibleAdapter
import java.io.Serializable

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class TranslationItem<T : BaseKt, VH : TranslationItem.ViewHolder, S : Serializable>(item: T?, @LayoutRes layoutId: Int = 0) :
    BaseItemKt<T, VH, S>(item, layoutId) {

    abstract class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItemKt.ViewHolder(view, adapter) {

        val adapter: TranslationAdapter

        init {
            this.adapter = adapter as TranslationAdapter
        }

        abstract fun <VH : ViewHolder, T : Translation, I : TranslationItem<T, VH, String>> bind(position: Int, item: I)
    }
}