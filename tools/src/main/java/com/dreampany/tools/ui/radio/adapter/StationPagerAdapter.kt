package com.dreampany.tools.ui.radio.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.extension.task
import com.dreampany.common.ui.adapter.BasePagerAdapter
import com.dreampany.common.ui.model.UiTask
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.RadioSubtype
import com.dreampany.tools.data.enums.radio.RadioType
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.ui.radio.fragment.StationsFragment

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationPagerAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

    init {
        addItems()
    }

    override fun getTitle(position : Int) : String {
        val fragment = getItem(position) ?: return Constants.Default.STRING
        val task = fragment.task ?: return Constants.Default.STRING
        if (task.state is RadioState) {
            return task.state.value
        }
        return Constants.Default.STRING
    }

    private fun addItems() {
        RadioState.values().forEach { state ->
            val task = UiTask<RadioType, RadioSubtype, RadioState, Action, Station>(
                RadioType.STATION,
                RadioSubtype.DEFAULT,
                state,
                Action.DEFAULT
            )
            addItem(
                com.dreampany.common.misc.extension.createFragment(
                    StationsFragment::class,
                    task
                )
            )
        }
    }
}