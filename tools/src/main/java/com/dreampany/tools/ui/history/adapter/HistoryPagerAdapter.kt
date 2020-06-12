package com.dreampany.tools.ui.history.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.ui.adapter.BasePagerAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.data.enums.history.HistoryAction
import com.dreampany.tools.data.enums.history.HistoryState
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.dreampany.tools.data.model.history.History
import com.dreampany.tools.ui.history.fragment.HistoriesFragment

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryPagerAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

    init {
        addItems()
    }

    override fun getTitle(position : Int) : String {
        val fragment = getItem(position) ?: return Constants.Default.STRING
        val task = fragment.task ?: return Constants.Default.STRING
        if (task.state is HistoryState) {
            val state : HistoryState = task.state as HistoryState
            return state.title
        }
        return Constants.Default.STRING
    }

    private fun addItems() {
        HistoryState.values().forEach { state ->
            when(state) {
                HistoryState.EVENT,
                HistoryState.BIRTH,
                HistoryState.DEATH->{
                    val task = UiTask<HistoryType, HistorySubtype, HistoryState, HistoryAction, History>(
                        HistoryType.HISTORY,
                        HistorySubtype.DEFAULT,
                        state,
                        HistoryAction.DEFAULT
                    )
                    addItem(
                        com.dreampany.framework.misc.exts.createFragment(
                            HistoriesFragment::class,
                            task
                        )
                    )
                }
            }

        }
    }
}