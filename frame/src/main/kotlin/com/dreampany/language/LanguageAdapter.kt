package com.dreampany.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.R
import com.dreampany.framework.util.TextUtil

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LanguageAdapter(
    val context: Context,
    val click: (Language) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    private val languages = ArrayList<Language>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(context)
        val view = inflator.inflate(R.layout.item_language, parent, false)
        return ViewHolder(view, click)
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = languages[position]
        holder.bind(language)
    }

    fun addLanguages(languages: List<Language>) {
        this.languages.clear()
        this.languages.addAll(languages)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, click: (Language) -> Unit) : RecyclerView.ViewHolder(view) {

        var viewText: AppCompatTextView

        init {
            viewText = view.findViewById(R.id.viewText)
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    val language = view.tag as Language
                    click(language)
                }
            })
        }

        fun bind(language: Language) {
            viewText.text = TextUtil.getString(itemView.context, R.string.language_text_format, language.country, language.code)
            itemView.setTag(language)
        }
    }
}