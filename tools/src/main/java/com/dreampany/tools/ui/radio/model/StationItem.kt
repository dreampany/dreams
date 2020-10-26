package com.dreampany.tools.ui.radio.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.context
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.databinding.StationItemBinding
import com.dreampany.tools.misc.exts.setRes
import com.dreampany.tools.misc.exts.setUrl
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationItem(
    val input: Station,
    val order: StationOrder
    ) : ModelAbstractBindingItem<Station, StationItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override val type: Int
        get() = R.id.adapter_station_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): StationItemBinding =
        StationItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: StationItemBinding, payloads: List<Any>) {
        binding.icon.setUrl(input.favicon)
        val statusRes = if (input.getLastCheckOk()) R.drawable.ic_status_live_24 else R.drawable.ic_status_dead_24
        binding.status.setRes(statusRes)

        binding.title.text = input.name
        binding.subtitle.text = input.subtitle(binding.context)

       /* if (input.getLastCheckOk()) {
            bind.labelType.primaryText = bind.context.getString(R.string.online)
            bind.labelType.setTriangleBackgroundColorResource(R.color.material_green500)
        } else {
            bind.labelType.primaryText = bind.context.getString(R.string.offline)
            bind.labelType.setTriangleBackgroundColorResource(R.color.material_red500)
        }

        bind.buttonFavorite.gone()*/

        /*if (adapter.isSelected(uiItem)) {
            title.setTextColor(getColor(R.color.material_black))
        } else {
            title.setTextColor(getColor(R.color.material_grey600))
        }*/

        //favorite.isLiked = uiItem.favorite
    }

    override fun unbindView(binding: StationItemBinding) {
        binding.title.text = input.name
        binding.subtitle.text = null
    }

    private fun Station.subtitle(context: Context): String {
        val subtitle = arrayListOf<String>()
        if (!this.getLastCheckOk()) {
            subtitle.add(context.getString(R.string.station_detail_broken))
        }
        if (this.bitrate > 0) {
            subtitle.add(context.getString(R.string.station_detail_bitrate, this.bitrate))
        }
        this.language?.run {
            if (isNotEmpty()) {
                subtitle.add(this)
            }
        }
        this.state?.run {
            subtitle.add(this)
        }
        return subtitle.joinToString(separator = Constant.Sep.SPACE.toString())
    }
}