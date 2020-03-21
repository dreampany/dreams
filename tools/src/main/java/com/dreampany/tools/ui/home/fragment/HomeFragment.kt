package com.dreampany.tools.ui.home.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.data.model.Response
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.common.ui.vm.factory.ViewModelFactory
import javax.inject.Inject
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.databinding.HomeFragmentBinding
import com.dreampany.tools.ui.home.vm.FeatureViewModel
import com.dreampany.tools.ui.model.FeatureItem
import timber.log.Timber

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HomeFragment
@Inject constructor() : BaseFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private lateinit var bind: HomeFragmentBinding
    private lateinit var vm: FeatureViewModel

    override fun layoutId(): Int  = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        vm.loadFeatures()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = getBinding()
        vm = ViewModelProvider(this, factory).get(FeatureViewModel::class.java)

        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun processResponse(response: Response<List<FeatureItem>, Type, Subtype>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<List<FeatureItem>, Type, Subtype>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: Throwable) {
        showDialogue(
            R.string.title_dialog_features,
            message = error.message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(features: List<FeatureItem>) {
        Timber.v("")
        //goToHomeScreen()
        /*showDialogue(
            R.string.title_dialog_registration,
            message = user.name,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )*/
    }
}