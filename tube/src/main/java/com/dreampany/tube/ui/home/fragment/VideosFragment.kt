package com.dreampany.tube.ui.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tube.R
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.model.Page
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.databinding.VideosFragmentBinding
import com.dreampany.tube.misc.Constants
import com.dreampany.tube.ui.home.activity.FavoriteVideosActivity
import com.dreampany.tube.ui.home.adapter.FastVideoAdapter
import com.dreampany.tube.ui.model.VideoItem
import com.dreampany.tube.ui.vm.VideoViewModel
import com.dreampany.tube.ui.player.VideoPlayerActivity
import com.dreampany.tube.ui.vm.SearchViewModel
import com.google.android.gms.location.LocationRequest
import com.patloew.colocation.CoLocation
import kotlinx.android.synthetic.main.content_recycler.view.*
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class VideosFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: VideosFragmentBinding
    private lateinit var searchVm: SearchViewModel
    private lateinit var vm: VideoViewModel
    private lateinit var adapter: FastVideoAdapter
    private lateinit var input: Page
    private lateinit var query: String

    override val layoutRes: Int = R.layout.videos_fragment
    override val menuRes: Int = R.menu.videos_menu
    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Page>
        input = task.input ?: return
        initUi()
        initRecycler(state)
    }

    override fun onStopUi() {
    }

    override fun onStart() {
        super.onStart()
        onRefresh()
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
        adapter.filter(newText)
        val value = newText.trimValue
        if (value.isNotEmpty()) {
            this.query = value
            ex.getUiHandler().removeCallbacks(runner)
            ex.getUiHandler().postDelayed(runner, 3000L)
        }
        return false
    }

    override fun onRefresh() {
        val order = pref.order
        if (adapter.isEmpty || adapter.order.equals(order).not()) {
            if (input.type.isRegion) {
                loadRegionVideos()
            } else if (input.type.isEvent) {
                vm.loadEventVideos(input.id, order, adapter.itemCount.toLong())
            } else if (input.type.isCategory) {
                vm.loadVideos(input.id, adapter.itemCount.toLong())
            } else if (input.type.isCustom) {
                vm.loadSearch(input.id, pref.order)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorites -> {
                openFavoritesUi()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onItemPressed(view: View, item: VideoItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openPlayerUi(item.input)
            }
            R.id.favorite -> {
                onFavoriteClicked(item.input)
            }
            else -> {

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadRegionVideos() {
        if (context.hasLocationPermission) {
            readRegionVideosSafe()
        } else {
            if (isFinishing) return
            runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
                readRegionVideosSafe()
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun readRegionVideosSafe() {
        val location = CoLocation.from(requireContext())
        val request = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        vm.viewModelScope.launch {
            /* val result = location.checkLocationSettings(request)
             when (request) {

             }*/
            val order = pref.order
            val data = location.getLastLocation()
            if (data == null) {
                vm.loadRegionVideos(input.id, order, 0)
            } else {
                vm.loadLocationVideos(data, order, 0)
            }
        }
    }

    private fun openPlayerUi(input: Video) {
        val task = UiTask(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.VIEW,
            input
        )
        open(VideoPlayerActivity::class, task)
    }

    private fun openFavoritesUi() {
        open(FavoriteVideosActivity::class)
    }

    private fun onFavoriteClicked(input: Video) {
        vm.toggleFavorite(input)
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        searchVm = createVm(SearchViewModel::class)
        vm = createVm(VideoViewModel::class)

        vm.subscribe(this, Observer { this.processResponse(it) })
        vm.subscribes(this, Observer { this.processResponses(it) })

        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_videos)
        bind.stateful.setStateView(StatefulLayout.State.OFFLINE, R.layout.content_offline_videos)
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastVideoAdapter(
            { currentPage: Int ->
                Timber.v("CurrentPage: %d", currentPage)
                onRefresh()
            }, this::onItemPressed
        )
        adapter.initRecycler(state, bind.layoutRecycler.recycler)
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<VideoItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<VideoItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, VideoItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, VideoItem>) {
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

    private fun processResults(result: List<VideoItem>?) {
        adapter.order = pref.order
        adapter.clearAll()
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processResult(result: VideoItem?) {
        if (result != null) {
            adapter.addItem(result)
        }
    }

    private val runner = Runnable {
        writeSearch()
    }

    private fun writeSearch() {
        if (isFinishing) return
        searchVm.write(query, Constants.Values.VIDEOS)
    }
}