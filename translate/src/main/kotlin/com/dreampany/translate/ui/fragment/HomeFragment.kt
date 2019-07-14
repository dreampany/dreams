package com.dreampany.translate.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.language.Language
import com.dreampany.translate.R
import com.dreampany.translate.data.model.TextTranslationRequest
import com.dreampany.translate.databinding.ContentTextTranslationBinding
import com.dreampany.translate.databinding.ContentTopStatusBinding
import com.dreampany.translate.databinding.FragmentHomeBinding
import com.dreampany.translate.ui.model.TranslationItem
import com.dreampany.translate.vm.TranslationViewModel
import cz.kinst.jakub.view.StatefulLayout
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@ActivityScope
class HomeFragment @Inject constructor() : BaseMenuFragment(), AdapterView.OnItemSelectedListener,
    TextWatcher {

    private val NONE = "none"
    private val SEARCH = "search"
    private val EMPTY = "empty"

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var bindHome: FragmentHomeBinding
    lateinit var bindStatus: ContentTopStatusBinding
    lateinit var bindTranslation: ContentTextTranslationBinding

    lateinit var vm: TranslationViewModel
    lateinit var adapter: ArrayAdapter<Language>

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onStartUi(state: Bundle?) {
        initView()
    }

    override fun onStopUi() {
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent) {
            bindTranslation.spinnerSource -> {
                vm.setSourceLanguage(adapter.getItem(position)!!)
            }
            bindTranslation.spinnerTarget -> {
                vm.setTargetLanguage(adapter.getItem(position)!!)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onClick(v: View) {
        when (v) {
            bindHome.fab -> {
                request(true, true)
            }
            bindTranslation.buttonSwitchLanguage -> {

            }
        }
    }

    private fun initView() {
        setTitle(R.string.home)
        bindHome = super.binding as FragmentHomeBinding
        bindStatus = bindHome.layoutTopStatus
        bindTranslation = bindHome.layoutTextTranslation

        vm = ViewModelProviders.of(this, factory).get(TranslationViewModel::class.java)
        vm.observeUiState(this, Observer { processUiState(it) })
        vm.observeOutput(this, Observer { processResponse(it) })

        bindHome.stateful.setStateView(
            NONE,
            LayoutInflater.from(getContext()).inflate(R.layout.item_none, null)
        )

        bindHome.fab.setOnClickListener(this)
        bindTranslation.buttonSwitchLanguage.setOnClickListener(this)

        val source = vm.getSourceLanguage()
        val target = vm.getTargetLanguage()
        val languages = vm.getLanguages()

        adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, languages)

        bindTranslation.spinnerSource.adapter = adapter
        bindTranslation.spinnerTarget.adapter = adapter

        bindTranslation.spinnerSource.setSelection(adapter.getPosition(source), true)
        bindTranslation.spinnerTarget.setSelection(adapter.getPosition(target), true)

        bindTranslation.spinnerSource.onItemSelectedListener = this
        bindTranslation.spinnerTarget.onItemSelectedListener = this
    }


    private fun processUiState(state: UiState) {
        when (state) {
            //UiState.NONE -> bindHome.stateful.setState(NONE)
            UiState.SHOW_PROGRESS -> if (!bindHome.layoutRefresh.isRefreshing()) {
                bindHome.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bindHome.layoutRefresh.isRefreshing()) {
                bindHome.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            //UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            //UiState.SEARCH -> bindHome.stateful.setState(SEARCH)
            //UiState.EMPTY -> bindHome.stateful.setState(SEARCH)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bindHome.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    fun processResponse(response: Response<TranslationItem<*, *, *>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress
            processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<TranslationItem<*, *, *>>
            processSingleSuccess(result.data)
        }
    }

    private fun processProgress(loading: Boolean) {
        if (loading) {
            vm.updateUiState(UiState.SHOW_PROGRESS)
        } else {
            vm.updateUiState(UiState.HIDE_PROGRESS)
        }
    }

    private fun processFailure(error: Throwable) {
        if (error is IOException || error.cause is IOException) {
            vm.updateUiState(UiState.OFFLINE)
        } else if (error is EmptyException) {
            vm.updateUiState(UiState.EMPTY)
        } else if (error is ExtraException) {
            vm.updateUiState(UiState.EXTRA)
        } else if (error is MultiException) {
            for (e in error.errors) {
                processFailure(e)
            }
        }
    }

    private fun processSingleSuccess(item: TranslationItem<*, *, *>) {
        processUiState(UiState.CONTENT)
    }

    private fun request(important: Boolean, progress: Boolean) {
        val source = vm.getSourceLanguage()
        val target = vm.getTargetLanguage()
        val input = bindTranslation.inputText.text.toString().trim()

        val request = TextTranslationRequest(source.code, target.code, input)
        request.important = important
        request.progress = progress
        vm.load(request)
    }
}