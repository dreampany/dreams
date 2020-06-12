package com.dreampany.tools.ui.radio.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.ui.adapter.BasePagerAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.data.enums.radio.RadioAction
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
            val state : RadioState = task.state as RadioState
            return state.title
        }
        return Constants.Default.STRING
    }

    private fun addItems() {
        RadioState.values().forEach { state ->
            if (state != RadioState.DEFAULT) {
                val task = UiTask<RadioType, RadioSubtype, RadioState, RadioAction, Station>(
                    RadioType.STATION,
                    RadioSubtype.DEFAULT,
                    state,
                    RadioAction.DEFAULT
                )
                addItem(
                    com.dreampany.framework.misc.exts.createFragment(
                        StationsFragment::class,
                        task
                    )
                )
            }
        }
    }
}