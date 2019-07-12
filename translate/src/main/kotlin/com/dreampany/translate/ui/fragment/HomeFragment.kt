package com.dreampany.translate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.dreampany.language.Language
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.util.TextUtil
import com.dreampany.translate.R
import com.dreampany.translate.databinding.ContentTopStatusBinding
import com.dreampany.translate.databinding.FragmentHomeBinding
import com.jaiselrahman.hintspinner.HintSpinnerAdapter
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@ActivityScope
class HomeFragment @Inject constructor() : BaseMenuFragment() {

    private val NONE = "none"
    private val SEARCH = "search"
    private val EMPTY = "empty"

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var bindHome: FragmentHomeBinding
    lateinit var bindStatus: ContentTopStatusBinding

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onStartUi(state: Bundle?) {
        initView()
    }

    override fun onStopUi() {
    }

    private fun initView() {
        setTitle(R.string.home)
        bindHome = super.binding as FragmentHomeBinding
        bindStatus = bindHome.layoutTopStatus


        bindHome.stateful.setStateView(
            NONE,
            LayoutInflater.from(getContext()).inflate(R.layout.item_none, null)
        )

        bindHome.fab.setOnClickListener(this)

        val languages = mutableListOf(Language.ENGLISH, Language.ARABIC)
        val adapter = object :HintSpinnerAdapter<Language>(context, languages, TextUtil.getString(context, R.string.select_language))  {
            override fun getLabelFor(input: Language): String {
                return input.country
            }
        }

        bindHome.layoutTextTranslation.spinnerSourceLanguage.setAdapter(adapter)
        bindHome.layoutTextTranslation.spinnerTargetLanguage.setAdapter(adapter)


        bindHome.layoutTextTranslation.spinnerSourceLanguage.setSelection(0)
        bindHome.layoutTextTranslation.spinnerTargetLanguage.setSelection(0)
    }
}