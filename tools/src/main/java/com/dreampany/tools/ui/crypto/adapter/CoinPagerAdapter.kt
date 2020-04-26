package com.dreampany.tools.ui.crypto.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.common.ui.adapter.BasePagerAdapter
import com.dreampany.common.ui.model.UiTask
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.ui.crypto.fragment.CoinInfoFragment

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinPagerAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

    init {
        addItems()
    }

    private fun addItems() {
        val task = UiTask(
            CryptoType.COIN,
            CryptoSubtype.INFO,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            null
        )
        addItem(
            com.dreampany.common.misc.extension.createFragment(
                CoinInfoFragment::class,
                task
            )
        )
    }
}