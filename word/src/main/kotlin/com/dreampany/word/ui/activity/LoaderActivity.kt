package com.dreampany.word.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.word.R
import com.dreampany.word.databinding.ActivityLoaderBinding
import com.dreampany.word.misc.Constants
import com.dreampany.word.ui.model.LoadItem
import com.dreampany.word.vm.LoaderViewModel
import com.github.nikartm.button.FitButton
import hugo.weaving.DebugLog
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by roman on 2019-06-10
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoaderActivity : BaseActivity() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    internal lateinit var vm: LoaderViewModel
    internal lateinit var bind: ActivityLoaderBinding
   // internal lateinit var progress: CircleProgressView
    internal lateinit var buttonDone: FitButton

    override fun getLayoutId(): Int {
        return R.layout.activity_loader
    }

    override fun isHomeUp(): Boolean {
        return false
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        vm.loads()
    }

    override fun onStopUi() {

    }

    private fun initView() {
        bind = super.binding as ActivityLoaderBinding
        vm = ViewModelProviders.of(this, factory).get(LoaderViewModel::class.java)
        vm.observeOutput(this, Observer { processSingleResponse(it) })
        //progress = bind.progress
        buttonDone = bind.buttonDone

        val total:Float = (Constants.Count.WORD_ALPHA).toFloat()
        //progress.maxValue = total
    }

    @DebugLog
    fun processSingleResponse(response: Response<LoadItem>) {
        if (response is Response.Progress<LoadItem>) {
            processProgress(response.loading)
        } else if (response is Response.Failure<LoadItem>) {
            processFailure(response.error)
        } else if (response is Response.Result<LoadItem>) {
            processSingleSuccess(response.data)
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

    private fun processSingleSuccess(item: LoadItem) {
        Timber.v("Result Single Load[%d]", item.item.current)
     //   progress.setValueAnimated(item.item.current.toFloat())
    }
}