package com.dreampany.tools.ui.crypto.fragment

import android.os.Bundle
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.enums.crypto.Times
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Graph
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.databinding.CryptoGraphFragmentBinding
import com.dreampany.tools.ui.crypto.vm.GraphViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class GraphFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: CryptoGraphFragmentBinding
    private lateinit var vm: GraphViewModel
    private lateinit var input: Coin

    override val layoutRes: Int = R.layout.crypto_graph_fragment

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return
        initUi()
        loadGraph()
    }

    override fun onStopUi() {
     }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(GraphViewModel::class)

        vm.subscribe(this,   { this.processResponse(it) })
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, Graph>) {
        if (response is Response.Progress) {
         } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, Graph>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResult(input: Graph?) {
        if (input != null) {
             bind.chart.data = input.data
             bind.chart.animateX(800)
        }
    }

    private fun loadGraph() {
        val currency = pref.graphCurrency
        vm.read(currency, input.slug, Times.MONTH)
    }

}