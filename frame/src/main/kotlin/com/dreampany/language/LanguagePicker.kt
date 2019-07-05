package com.dreampany.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.dreampany.frame.R
import com.dreampany.frame.data.enums.Language
import com.dreampany.frame.databinding.DialogLanguagePickerBinding


/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LanguagePicker : DialogFragment() {

    private lateinit var binding: DialogLanguagePickerBinding
    private val languages = mutableListOf<Language>()
    private lateinit var click: (Language) -> Unit
    private lateinit var adapter: LanguageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_language_picker, container, false)
        binding.setLifecycleOwner(this)
        initView()
        return binding.root
    }

    override fun dismiss() {
        if (dialog != null) {
            super.dismiss()
        } else {
            fragmentManager?.popBackStack()
        }
    }

    companion object {
        fun newInstance(title: String, languages: List<Language>) {

        }
    }

    fun setCallback(click: (Language) -> Unit) {
        this.click = click
    }

    fun setLanguages(languages: List<Language>) {
        this.languages.clear()
        this.languages.addAll(languages)
        adapter.addLanguages(languages)
    }

    private fun initView() {
        val args = arguments
        if (args != null && dialog != null) {
            val dialogTitle = args.getString("title")
            dialog!!.setTitle(dialogTitle)

            val width = resources.getDimensionPixelSize(R.dimen.dialog_width)
            val height = resources.getDimensionPixelSize(R.dimen.dialog_height)
            dialog!!.window!!.setLayout(width, height)
        }
        adapter = LanguageAdapter(context!!, click)
    }
}