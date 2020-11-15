package com.dreampany.tools.ui.crypto.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.framework.ui.adapter.BasePagerAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.ui.crypto.fragment.GraphFragment
import com.dreampany.tools.ui.crypto.fragment.InfoFragment
import com.dreampany.tools.ui.crypto.fragment.TickersFragment

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PageAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

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
        val graph = UiTask(
            Type.COIN,
            Subtype.GRAPH,
            State.DEFAULT,
            Action.DEFAULT,
            input
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                InfoFragment::class,
                info
            ),
            R.string.title_coin_info,
            true
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                TickersFragment::class,
                market
            ),
            R.string.title_coin_markets,
            true
        )
        /*addItem(
            com.dreampany.framework.misc.exts.createFragment(
                GraphFragment::class,
                market
            ),
            R.string.title_coin_graph,
            true
        )*/
    }
}