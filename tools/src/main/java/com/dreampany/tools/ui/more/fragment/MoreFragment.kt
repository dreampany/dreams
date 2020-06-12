package com.dreampany.tools.ui.more.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.moreApps
import com.dreampany.framework.misc.exts.rateUs
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.home.Subtype
import com.dreampany.tools.data.enums.home.Type
import com.dreampany.tools.data.model.more.More
import com.dreampany.tools.databinding.RecyclerFragmentBinding
import com.dreampany.tools.ui.more.adapter.FastMoreAdapter
import com.dreampany.tools.ui.more.model.MoreItem
import com.dreampany.tools.ui.more.vm.MoreViewModel
import com.mikepenz.aboutlibraries.LibsBuilder
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class MoreFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: MoreViewModel

    private lateinit var adapter: FastMoreAdapter

    override val layoutRes: Int = R.layout.recycler_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        if (adapter.isEmpty)
            vm.loadMores()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = getBinding()
        if (!::vm.isInitialized) {
            vm = createVm(MoreViewModel::class)
            vm.subscribes(this, Observer { this.processResponse(it) })
        }
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastMoreAdapter(clickListener = { item: MoreItem ->
                Timber.v("MoreItem: %s", item.item.toString())
                onPressed(item.item)
            })

            adapter.initRecycler(
                state,
                bind.layoutRecycler.recycler
            )
        }
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<MoreItem>>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<MoreItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: Throwable) {
        showDialogue(
            R.string.title_dialog_more,
            message = error.message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(result: List<MoreItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }
    }

    private fun onPressed(item: More) {
        when (item.subtype) {
            Subtype.APPS -> {
                activity.moreApps(getString(R.string.id_google_play))
            }
            Subtype.RATE_US -> {
                activity.rateUs()
            }
            Subtype.FEEDBACK -> {
                activity.rateUs()
                //activity.moreApps(getString(R.string.id_google_play))
            }
            Subtype.LICENSE -> {
                activity.moreApps(getString(R.string.id_google_play))
            }
            Subtype.ABOUT -> {
                //activity.moreApps(getString(R.string.id_google_play))
                LibsBuilder().start(requireContext())
            }
        }

    }
}