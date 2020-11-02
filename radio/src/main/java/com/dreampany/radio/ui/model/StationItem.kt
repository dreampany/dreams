package com.dreampany.radio.ui.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.context
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.databinding.StationItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.dreampany.radio.R
import com.dreampany.radio.misc.setRes
import com.dreampany.radio.misc.setUrl

/**
 * Created by roman on 2/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationItem(
    val input: Station
) : ModelAbstractBindingItem<Station, StationItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override val type: Int
        get() = R.id.adapter_station_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): StationItemBinding =
        StationItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: StationItemBinding, payloads: List<Any>) {
        binding.icon.setUrl(input.favicon)
        val statusRes =
            if (input.getLastCheckOk()) R.drawable.ic_status_live_24 else R.drawable.ic_status_dead_24
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
        this.languages?.joinToString()?.run {
            if (isNotEmpty()) {
                subtitle.add(this)
            }
        }
        this.state?.run {
            subtitle.add(this)
        }
        return subtitle.joinToString(separator = com.dreampany.framework.misc.constant.Constant.Sep.SPACE.toString())
    }
}