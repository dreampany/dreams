package com.dreampany.tube.misc

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener

/**
 * Created by roman on 17/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class PlayerListener : YouTubePlayerListener {

    override fun onApiChange(player: YouTubePlayer) {

    }

    override fun onCurrentSecond(player: YouTubePlayer, second: Float) {
    }

    override fun onError(player: YouTubePlayer, error: PlayerConstants.PlayerError) {
    }

    override fun onPlaybackQualityChange(
        player: YouTubePlayer,
        quality: PlayerConstants.PlaybackQuality
    ) {
    }

    override fun onPlaybackRateChange(
        player: YouTubePlayer,
        rate: PlayerConstants.PlaybackRate
    ) {
    }

    override fun onReady(player: YouTubePlayer) {
    }

    override fun onStateChange(player: YouTubePlayer, state: PlayerConstants.PlayerState) {
    }

    override fun onVideoDuration(player: YouTubePlayer, duration: Float) {
    }

    override fun onVideoId(player: YouTubePlayer, videoId: String) {
    }

    override fun onVideoLoadedFraction(player: YouTubePlayer, loadedFraction: Float) {
    }
}