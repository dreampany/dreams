package com.dreampany.tools.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dreampany.framework.misc.FragmentScope
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.tools.R
import javax.inject.Inject

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class SmallPlayerFragment
@Inject constructor() : BaseFragment() {

    enum class Mode {
        HEADER, PLAYER
    }

    private lateinit var mode: Mode
    private lateinit var receiver: BroadcastReceiver


    init {
        mode = Mode.PLAYER
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_small_player
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {


            }
        }
    }
}