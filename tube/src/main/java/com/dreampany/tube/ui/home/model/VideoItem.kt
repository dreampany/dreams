package com.dreampany.tube.ui.home.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.*
import com.dreampany.tube.R
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.databinding.VideoItemBinding
import com.dreampany.tube.misc.setUrl
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import java.util.*

/**
 * Created by roman on 1/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoItem(
    val input: Video,
    var favorite: Boolean = false
) : ModelAbstractBindingItem<Video, VideoItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as VideoItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_video_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): VideoItemBinding =
        VideoItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: VideoItemBinding, payloads: List<Any>) {
        bind.thumb.setUrl(input.thumbnail)
        bind.definition.text = input.definition
        bind.definition.visible(input.definition.isNullOrEmpty().not())
        bind.title.text = input.title
        bind.info.text = bind.context.getString(
            R.string.video_info_format,
            input.channelTitle,
            input.viewCount.count,
            input.publishedAt.time
        )
        if (input.isLive || input.duration.isNullOrEmpty()) {
            bind.duration.text = input.liveBroadcastContent?.toUpperCase(Locale.getDefault())
        } else {
            bind.duration.text = input.duration
        }
        bind.favorite.isLiked = favorite
    }

    override fun unbindView(binding: VideoItemBinding) {

    }
}