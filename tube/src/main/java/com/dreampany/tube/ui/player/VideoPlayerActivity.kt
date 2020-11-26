package com.dreampany.tube.ui.player

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.data.model.Time
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tube.R
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.databinding.VideoPlayerActivityBinding
import com.dreampany.tube.misc.PlayerListener
import com.dreampany.tube.ui.home.adapter.FastVideoAdapter
import com.dreampany.tube.ui.model.VideoItem
import com.dreampany.tube.ui.vm.VideoViewModel
import com.dreampany.tube.ui.vm.TimeViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import kotlinx.android.synthetic.main.content_recycler.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 7/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoPlayerActivity : InjectActivity() {

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: VideoPlayerActivityBinding
    private lateinit var timeVm: TimeViewModel
    private lateinit var vm: VideoViewModel
    private lateinit var adapter: FastVideoAdapter
    private lateinit var input: Video
    private lateinit var player: YouTubePlayer
    private var currentSeconds = 0L

    override val layoutRes: Int = R.layout.video_player_activity
    override val menuRes: Int = R.menu.videos_menu
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, "VideoPlayerActivity")

            params.put(Constant.Event.activity(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Video>
        input = task.input ?: return
        initUi()
        initRecycler(state)
        updateUi()
        vm.loadVideo(input)
        vm.loadRelated(input.id, pref.order)
    }

    override fun onStopUi() {
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
        return false
    }

    private fun onItemPressed(view: View, item: VideoItem) {
        when (view.id) {
            R.id.layout -> {
                reInit(item)
            }
            R.id.favorite -> {
                onFavoriteClicked(item.input)
            }
            else -> {

            }
        }
    }

    private fun onFavoriteClicked(input: Video) {
        vm.toggleFavorite(input)
    }

    private fun reInit(item: VideoItem) {
        input = item.input
        updateUi()
        timeVm.read(input.id, Type.VIDEO, Subtype.DEFAULT, State.DEFAULT)
        vm.loadRelated(input.id, pref.order)
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        timeVm = createVm(TimeViewModel::class)
        vm = createVm(VideoViewModel::class)

        timeVm.subscribe(this, { this.processTimeResponse(it) })
        vm.subscribe(this, { this.processResponse(it) })
        vm.subscribes(this, { this.processResponses(it) })

        bind.swipe.init(this)
        bind.swipe.disable()

        bind.favorite.setOnSafeClickListener {
            onFavoriteClicked(input)
        }

        lifecycle.addObserver(bind.player)
        bind.player.getPlayerUiController().apply {
            showBufferingProgress(true)
            showMenuButton(true)
        }
        bind.player.enableBackgroundPlayback(true)
        bind.player.addYouTubePlayerListener(object : PlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                this@VideoPlayerActivity.player = player
                timeVm.read(input.id, Type.VIDEO, Subtype.DEFAULT, State.DEFAULT)
            }

            override fun onCurrentSecond(player: YouTubePlayer, second: Float) {
                if (currentSeconds == second.toLong()) return
                currentSeconds = second.toLong()
                timeVm.write(input.id, Type.VIDEO, Subtype.DEFAULT, State.DEFAULT, currentSeconds)
            }
        })
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

    private fun updateUi() {
        bind.title.text = input.title.html
        val count = input.viewCount.count
        val info = getString(
            R.string.video_info_format,
            count,
            input.publishedAt.time
        )
        val builder = SpannableStringBuilder()
        val span = SpannableString(info)
        span.setSpan(
            ForegroundColorSpan(bind.color(R.color.textColorPrimary)),
            0,
            count.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            count.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        bind.info.text = span

        bind.player.getPlayerUiController().apply {
            enableLiveVideoUi(input.isLive)
            setVideoTitle(input.title.value)
        }
    }

    private fun processTimeResponse(response: Response<Type, Subtype, State, Action, Time>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, Time>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
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

    private fun processResult(result: Time?) {
        val time = result?.time.value.toFloat()
        if (::player.isInitialized)
            player.loadOrCueVideo(lifecycle, input.id, time)
    }

    private fun processResults(result: List<VideoItem>?) {
        adapter.order = pref.order
        adapter.clearAll()
        if (result != null) {
            adapter.addItems(result)
        }
    }

    private fun processResult(result: VideoItem?) {
        if (result != null) {
            if (input.id == result.input.id) {
                bind.favorite.isLiked = result.favorite
            } else {
                adapter.addItem(result)
            }
        }
    }
}