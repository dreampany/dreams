package com.dreampany.tube.ui.home.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tube.databinding.RecyclerActivityBinding
import com.dreampany.tube.ui.home.adapter.FastVideoAdapter
import com.dreampany.tube.ui.home.vm.VideoViewModel
import com.dreampany.tube.R
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.manager.AdManager
import com.dreampany.tube.ui.home.model.VideoItem
import com.dreampany.tube.ui.player.VideoPlayerActivity
import kotlinx.android.synthetic.main.content_recycler.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 11/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FavoriteVideosActivity : InjectActivity() {

    @Inject
    internal lateinit var ad: AdManager

    private lateinit var bind: RecyclerActivityBinding
    private lateinit var vm: VideoViewModel
    private lateinit var adapter: FastVideoAdapter

    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.recycler_activity
    override val toolbarId: Int = R.id.toolbar
    override val menuRes: Int = R.menu.search_menu
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constants.Param.PACKAGE_NAME, packageName)
            param.put(Constants.Param.VERSION_CODE, versionCode)
            param.put(Constants.Param.VERSION_NAME, versionName)
            param.put(Constants.Param.SCREEN, "FavoriteVideosActivity")

            params.put(Constants.Event.ACTIVITY, param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        initRecycler(state)
        onRefresh()
    }

    override fun onStopUi() {

    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ad.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return false
    }

    override fun onRefresh() {
        loadVideos()
    }

    private fun onItemPressed(view: View, item: VideoItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openPlayerUi(item)
            }
            else -> {

            }
        }
    }

    private fun loadVideos() {
        if (adapter.isEmpty)
            vm.loadFavoriteVideos()
        else
            bind.swipe.refresh(false)
    }


    private fun initAd() {
        ad.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_favorite_videos)

        vm = createVm(VideoViewModel::class)
        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastVideoAdapter(
            { currentPage ->
                Timber.v("CurrentPage: %d", currentPage)
                //onRefresh()
            }, this::onItemPressed
        )
        adapter.initRecycler(state, bind.layoutRecycler.recycler)
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<VideoItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<VideoItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
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

    private fun processResults(result: List<VideoItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun openPlayerUi(item: VideoItem) {
        val task = UiTask(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.VIEW,
            item.input
        )
        open(VideoPlayerActivity::class, task)
    }
}