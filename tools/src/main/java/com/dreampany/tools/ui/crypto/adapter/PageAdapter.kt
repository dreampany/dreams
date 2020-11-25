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
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.ui.crypto.fragment.DetailsFragment
import com.dreampany.tools.ui.crypto.fragment.TickersFragment

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PageAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

    private lateinit var details : DetailsFragment

    fun updateUi(input: Coin, quote: Quote) {
        details.updateUi(input, quote)
    }

    fun addItems(input: Coin) {
        val detailsTask = UiTask(
            Type.COIN,
            Subtype.DETAILS,
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
        details =  com.dreampany.framework.misc.exts.createFragment(
            DetailsFragment::class,
            detailsTask
        )
        addItem(
           details,
            R.string.title_coin_details,
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