package com.dreampany.tube.ui.model

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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

    override fun bindView(binding: VideoItemBinding, payloads: List<Any>) {
        binding.thumb.setUrl(input.thumbnail)
        binding.definition.text = input.definition.html
        binding.definition.visible(input.definition.isNullOrEmpty().not())
        binding.channel.text = input.channelTitle
        binding.title.text = input.title.html
        val count = input.viewCount.count
        val info = binding.context.getString(
            R.string.video_info_format,
            count,
            input.publishedAt.time
        )
        val builder = SpannableStringBuilder()
        val span = SpannableString(info)
        span.setSpan(
            ForegroundColorSpan(binding.color(R.color.textColorPrimary)),
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
        binding.info.text = span

        if (input.isLive || input.duration.isNullOrEmpty()) {
            binding.duration.text = input.liveBroadcastContent?.toUpperCase(Locale.getDefault())
        } else {
            binding.duration.text = input.duration
        }
        binding.favorite.isLiked = favorite
    }

    override fun unbindView(binding: VideoItemBinding) {

    }
}