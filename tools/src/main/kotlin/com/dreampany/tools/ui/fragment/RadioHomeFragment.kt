package com.dreampany.tools.ui.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentRadioHomeBinding
import javax.inject.Inject

/**
 * Created by roman on 2019-10-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class RadioHomeFragment
@Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var bind: FragmentRadioHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding

    override fun getLayoutId(): Int {
        return R.layout.fragment_radio_home
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_radio
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = super.binding as FragmentRadioHomeBinding
        bindStatus = bind.layoutTopStatus

    }
}