package com.dreampany.word.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.frame.util.NumberUtil
import com.dreampany.word.R
import com.dreampany.word.databinding.ActivityLoaderBinding
import com.dreampany.word.ui.model.LoadItem
import com.dreampany.word.vm.LoaderViewModel
import hugo.weaving.DebugLog
import java.io.IOException
import javax.inject.Inject


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class                                                                                                                                              LoaderActivity : BaseActivity() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    internal lateinit var vm: LoaderViewModel

    lateinit var bind: ActivityLoaderBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_loader
    }

    override fun isHomeUp(): Boolean {
        return false
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        vm.loadCommons()
    }

    override fun onStopUi() {
    }

    fun initView() {
        bind = binding as ActivityLoaderBinding
        bind.buttonDone.setOnClickListener({
            openActivity(NavigationActivity::class.java)
        })
        vm = ViewModelProviders.of(this, factory).get<LoaderViewModel>(LoaderViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processResponse(it) })
    }

    private fun processUiState(state: UiState?) {
        when (state) {
            UiState.SHOW_PROGRESS -> {
/*                bind.preProgress.visibility = View.VISIBLE
                bind.postProgress.visibility = View.GONE
                bind.textStatus.visibility = View.VISIBLE
                val text = TextUtil.getString(application, R.string.extract, Constants.Count.WORD_COMMON)
                bind.textStatus.text = text*/
            }
            UiState.HIDE_PROGRESS -> {
/*                bind.preProgress.visibility = View.GONE
                bind.postProgress.visibility = View.VISIBLE
                bind.textStatus.visibility = View.VISIBLE

 *//*               if (bind.postProgress.progress + 10 >= bind.postProgress.maxProgress) {

                } else {

                }*//*

                val text = TextUtil.getString(application, R.string.extract_completed, Constants.Count.WORD_COMMON)
                bind.textStatus.text = text*/
            }
        }
    }

    @DebugLog
    fun processResponse(response: Response<LoadItem>?) {
        if (response is Response.Progress<*>) {
            val (loading) = response
            processProgress(loading)
        } else if (response is Response.Failure<*>) {
            val (error) = response
            processFailure(error)
        } else if (response is Response.Result<LoadItem>) {
            val result = response
            processSuccess(result.data);
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

    @DebugLog
    private fun processSuccess(item: LoadItem) {
        bind.progress.setValueAnimated(NumberUtil.percentage(item.item.current, item.item.total).toFloat())
        if (item.item.current == item.item.total) {
            bind.buttonDone.visibility = View.VISIBLE
        }
    }
}