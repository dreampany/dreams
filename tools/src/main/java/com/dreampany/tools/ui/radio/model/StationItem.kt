package com.dreampany.tools.ui.radio.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.extension.context
import com.dreampany.framework.misc.extension.gone
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.databinding.StationItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationItem(
    val item: Station,
    val order: StationOrder
    ) : ModelAbstractBindingItem<Station, StationItemBinding>(item) {

    override fun hashCode(): Int = item.hashCode()

    override fun equals(other: Any?): Boolean = item.equals(other)

    override val type: Int
        get() = R.id.adapter_station_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): StationItemBinding =
        StationItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: StationItemBinding, payloads: List<Any>) {
        bind.viewTitle.text = item.name
        bind.viewSubtitle.text = getSubtitle(bind.context, item)

        if (item.getLastCheckOk()) {
            bind.labelType.primaryText = bind.context.getString(R.string.online)
            bind.labelType.setTriangleBackgroundColorResource(R.color.material_green500)
        } else {
            bind.labelType.primaryText = bind.context.getString(R.string.offline)
            bind.labelType.setTriangleBackgroundColorResource(R.color.material_red500)
        }

        bind.buttonFavorite.gone()

        /*if (adapter.isSelected(uiItem)) {
            title.setTextColor(getColor(R.color.material_black))
        } else {
            title.setTextColor(getColor(R.color.material_grey600))
        }*/

        //favorite.isLiked = uiItem.favorite
    }

    override fun unbindView(binding: StationItemBinding) {

    }

    private fun getSubtitle(context: Context, station: Station): String {
        val subtitle = arrayListOf<String>()
        if (!station.getLastCheckOk()) {
            subtitle.add(context.getString(R.string.station_detail_broken))
        }
        if (station.bitrate > 0) {
            subtitle.add(context.getString(R.string.station_detail_bitrate, station.bitrate))
        }
        station.language?.run {
            if (isNotEmpty()) {
                subtitle.add(this)
            }
        }
        station.state?.run {
            subtitle.add(this)
        }
        return subtitle.joinToString(separator = Constants.Sep.SPACE.toString())
    }
}