package com.dreampany.word.ui.model

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.ui.model.BaseItemKt
import com.dreampany.word.R
import com.dreampany.word.data.enums.ItemState
import com.dreampany.word.data.model.Word
import com.dreampany.word.ui.adapter.WordAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordItem private constructor(item: Word, @LayoutRes layoutId: Int = 0) :
    BaseItemKt<Word, WordItem.ViewHolder, String>(item, layoutId) {

    var recent: Boolean = false
    val states: MutableSet<ItemState> = mutableSetOf()
    val translations: MutableMap<String, String> = mutableMapOf()
    var translation: String? = null

    companion object {
        fun getItem(item: Word): WordItem {
            return WordItem(item, R.layout.item_word)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.id.startsWith(constraint, true);
    }

    fun addState(state: ItemState) {
        states.add(state);
    }

    fun hasTranslation(language: String?): Boolean {
        if (language == null) {
            return false
        }
        return translations.containsKey(language)
    }

    fun addTranslation(language: String, translatedWord: String) {
        translations.put(language, translatedWord)
    }

    fun getTranslationBy(language: String?): String? {
        if (hasTranslation(language)) {
            return translations.get(language)
        }
        return null
    }


    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItemKt.ViewHolder(view, adapter) {

        private var adapter: WordAdapter
        private var viewWord: TextView
        private var viewPartOfSpeech: TextView
        private var viewPronunciation: TextView

        init {
            this.adapter = adapter as WordAdapter
            viewWord = view.findViewById(R.id.text_word)
            viewPartOfSpeech = view.findViewById(R.id.text_part_of_speech)
            viewPronunciation = view.findViewById(R.id.text_part_of_speech)
        }

        override fun <VH : BaseItemKt.ViewHolder, T : BaseKt, S : Serializable, I : BaseItemKt<T, VH, S>>
                bind(position: Int, item: I) {
            val wordItem = item as WordItem
            val word = wordItem.item

            viewWord.text = word.id
            viewPartOfSpeech.setText(word.getPartOfSpeech())
            viewPronunciation.setText(word.pronunciation)
        }
    }
}