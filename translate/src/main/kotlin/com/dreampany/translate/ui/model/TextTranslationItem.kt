package com.dreampany.translate.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.data.model.Translation
import eu.davidea.flexibleadapter.FlexibleAdapter

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class TextTranslationItem(item: TextTranslation?, @LayoutRes layoutId: Int = 0) :
    TranslationItem<TextTranslation, TextTranslationItem.ViewHolder, String> (item, layoutId) {

     class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        TranslationItem.ViewHolder(view, adapter) {

         override fun <VH : TranslationItem.ViewHolder, T : Translation, I : TranslationItem<T, VH, String>> bind(
             position: Int,
             item: I
         ) {
             TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
         }

    }
}