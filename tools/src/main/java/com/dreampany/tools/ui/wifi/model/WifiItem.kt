package com.dreampany.tools.ui.wifi.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.adapter.value
import com.dreampany.framework.misc.extension.color
import com.dreampany.framework.misc.extension.string
import com.dreampany.tools.R
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.databinding.WifiItemBinding
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiItem
private constructor(
    val input: Wifi,
    var favorite: Boolean
) : ModelAbstractBindingItem<Wifi, WifiItemBinding>(input) {

    companion object {
        fun get(
            input: Wifi,
            favorite: Boolean = false
        ): WifiItem = WifiItem(input, favorite)
    }

    override fun hashCode(): Int = Objects.hashCode(input.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as WifiItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int = R.id.adapter_wifi_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): WifiItemBinding =
        WifiItemBinding.inflate(inflater, parent, false)


    override fun bindView(bind: WifiItemBinding, payloads: List<Any>) {
        val strength = input.signal?.strength
        bind.icon.setImageResource(strength?.imageRes.value())
        bind.icon.setColorFilter(bind.color(strength?.colorRes.value()))
        bind.textSsid.text = String.format(bind.string(R.string.format_ssid_bssid), input.ssid, input.bssid)
        bind.textLevel.text = String.format(bind.string(R.string.format_wifi_level), input.signal?.level)
        bind.textLevel.setTextColor(bind.color(strength?.colorRes.value()))
    }

    override fun unbindView(binding: WifiItemBinding) {

    }
}