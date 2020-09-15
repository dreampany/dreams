package com.dreampany.tube.ui.player

import android.os.Bundle
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tube.R
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.databinding.VideoPlayerActivityBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo

/**
 * Created by roman on 7/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoPlayerActivity : InjectActivity() {

    private lateinit var bind: VideoPlayerActivityBinding
    private lateinit var video: Video

    override val layoutRes: Int = R.layout.video_player_activity

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constants.Param.PACKAGE_NAME, packageName)
            param.put(Constants.Param.VERSION_CODE, versionCode)
            param.put(Constants.Param.VERSION_NAME, versionName)
            param.put(Constants.Param.SCREEN, "VideoPlayerActivity")

            params.put(Constants.Event.ACTIVITY, param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Video>
        video = task.input ?: return
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (!::bind.isInitialized) {
            bind = getBinding()
            lifecycle.addObserver(bind.player)
            bind.player.getPlayerUiController().apply {
                enableLiveVideoUi(video.isLive)
                setVideoTitle(video.title.value)
                showBufferingProgress(true)
                showMenuButton(true)
            }
            bind.player.enableBackgroundPlayback(true)
            //bind.player.getPlayerUiController().enableLiveVideoUi(video.isLive)
            bind.player.addYouTubePlayerListener(object : YouTubePlayerListener {
                override fun onApiChange(youTubePlayer: YouTubePlayer) {

                }

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                }

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) {
                }

                override fun onPlaybackQualityChange(
                    youTubePlayer: YouTubePlayer,
                    playbackQuality: PlayerConstants.PlaybackQuality
                ) {
                }

                override fun onPlaybackRateChange(
                    player: YouTubePlayer,
                    playbackRate: PlayerConstants.PlaybackRate
                ) {
                }

                override fun onReady(player: YouTubePlayer) {
                    player.loadOrCueVideo(lifecycle, video.id, 0f)
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                }

                override fun onVideoDuration(player: YouTubePlayer, duration: Float) {
                }

                override fun onVideoId(player: YouTubePlayer, videoId: String) {
                }

                override fun onVideoLoadedFraction(
                    youTubePlayer: YouTubePlayer,
                    loadedFraction: Float
                ) {

                }

            })
        }
    }
}