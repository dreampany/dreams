package com.dreampany.crypto.ui.home.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.ui.home.fragment.InfoFragment
import com.dreampany.crypto.ui.home.fragment.MarketFragment
import com.dreampany.framework.ui.adapter.BasePagerAdapter
import com.dreampany.framework.ui.model.UiTask

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinPagerAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

    fun addItems(input: Coin) {
        val info = UiTask(
            Type.COIN,
            Subtype.INFO,
            State.DEFAULT,
            Action.DEFAULT,
            input
        )
        val market = UiTask(
            Type.COIN,
            Subtype.MARKET,
            State.DEFAULT,
            Action.DEFAULT,
            input
        )
        addItem(
            com.dreampany.framework.misc.extension.createFragment(
                InfoFragment::class,
                info
            ),
            R.string.title_coin_info,
            true
        )
        addItem(
            com.dreampany.framework.misc.extension.createFragment(
                MarketFragment::class,
                market
            ),
            R.string.title_coin_market,
            true
        )
    }
}