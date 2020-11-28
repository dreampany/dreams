package com.dreampany.radio.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.radio.data.source.pref.Prefs
import com.dreampany.stateful.StatefulLayout
import kotlinx.android.synthetic.main.content_recycler.view.*
import timber.log.Timber
import javax.inject.Inject
import com.dreampany.radio.R
import com.dreampany.radio.data.enums.Action
import com.dreampany.radio.data.enums.State
import com.dreampany.radio.data.enums.Subtype
import com.dreampany.radio.data.enums.Type
import com.dreampany.radio.databinding.SearchFragmentBinding
import com.dreampany.radio.manager.RadioPlayerManager
import com.dreampany.radio.misc.Constants
import com.dreampany.radio.ui.adapter.FastStationAdapter
import com.dreampany.radio.ui.model.StationItem
import com.dreampany.radio.ui.vm.PageViewModel
import com.dreampany.radio.ui.vm.SearchViewModel
import com.dreampany.radio.ui.vm.StationViewModel

/**
 * Created by roman on 30/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class SearchFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: Prefs

    @Inject
    internal lateinit var player: RadioPlayerManager

    private lateinit var bind: SearchFragmentBinding
    private lateinit var searchVm: SearchViewModel
    private lateinit var pageVm: PageViewModel
    private lateinit var vm: StationViewModel
    private lateinit var adapter: FastStationAdapter
    private lateinit var query: String

    override val layoutRes: Int = R.layout.search_fragment
    override val menuRes: Int = R.menu.search_menu
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, parentRef.packageName)
            param.put(Constant.Param.VERSION_CODE, parentRef.versionCode)
            param.put(Constant.Param.VERSION_NAME, parentRef.versionName)
            param.put(Constant.Param.SCREEN, "SearchFragment")

            params.put(Constant.Event.fragment(context), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        bind.stateful.setState(StatefulLayout.State.DEFAULT)
        player.bind()
    }

    override fun onStopUi() {
        player.unbind()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Constants.Service.PLAYER_SERVICE_UPDATE)
        bindLocalCast(serviceUpdateReceiver, filter)
    }

    override fun onPause() {
        debindLocalCast(serviceUpdateReceiver)
        if (!player.isPlaying()) {
            player.destroy()
        }
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::adapter.isInitialized) {
            var outState = outState
            outState = adapter.saveInstanceState(outState)
            super.onSaveInstanceState(outState)
            return
        }
        super.onSaveInstanceState(outState)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //adapter.filter(newText)
        bind.layoutMake.hide()
        /*if (newText.isNullOrEmpty().not()) {
            searchVideos(newText.value)
        }*/
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val value = query.trimValue
        if (value.isNotEmpty()) {
            this.query = value
            search(value)
            writeSearch()
        }
        return false
    }

    private val serviceUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updatePlaying()
        }
    }

    private fun onItemPressed(view: View, input: StationItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                 player.play(input.input)
            }
            /*R.id.favorite -> {
                onFavoriteClicked(item)
            }*/
            else -> {

            }
        }
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        searchVm = createVm(SearchViewModel::class)
        pageVm = createVm(PageViewModel::class)
        vm = createVm(StationViewModel::class)

        vm.subscribe(this, { this.processResponse(it) })
        vm.subscribes(this, { this.processResponses(it) })

        bind.swipe.init(this)
        bind.stateful.setStateView(
            StatefulLayout.State.DEFAULT,
            R.layout.content_default_search_stations
        )
        bind.stateful.setStateView(
            StatefulLayout.State.EMPTY,
            R.layout.content_empty_search_stations
        )
        bind.stateful.setStateView(StatefulLayout.State.OFFLINE, R.layout.content_offline_stations)

        bind.layoutMake.setOnSafeClickListener {
            writePage()
            bind.layoutMake.hide()
        }
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastStationAdapter(
            { currentPage: Int ->
                Timber.v("CurrentPage: %d", currentPage)
                onRefresh()
            }, this::onItemPressed
        )
        adapter.initRecycler(state, bind.layoutRecycler.recycler)
    }


/*    private fun onFavoriteClicked(item: VideoItem) {
        vm.toggleFavorite(item.input)
    }*/

    private fun search(query: String) {
        vm.search(query, pref.order, 0)
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<StationItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<StationItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, StationItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, StationItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processError(error: SmartError) {
        if (error.hostError) {
            if (adapter.isEmpty) {
                bind.stateful.setState(StatefulLayout.State.OFFLINE)
            } else {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
            }
        }

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

    private fun processResults(result: List<StationItem>?) {
        adapter.clearAll()
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
            bind.layoutMake.hide()
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
            bind.layoutMake.show()
        }
    }

    private fun processResult(result: StationItem?) {
        if (result != null) {
            //adapter.addItem(result)
        }
    }

    private fun writePage() {
        pageVm.write(query)
    }

    private fun writeSearch() {
        searchVm.write(query, Constants.Values.SEARCH)
    }

    private fun updatePlaying() {
        if (player.isPlaying()) {
            player.getStation()?.run {
                /*mapper.getUiItem(this.id)?.run {
                    adapter.setSelection(this, true)
                }*/
            }
        } else {
            //adapter.clearSelection()
        }
    }
}