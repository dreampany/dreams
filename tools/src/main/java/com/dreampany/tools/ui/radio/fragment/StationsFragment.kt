package com.dreampany.tools.ui.radio.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.inject.annote.FragmentScope
import com.dreampany.common.ui.fragment.InjectFragment
import javax.inject.Inject

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class StationsFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    override fun onStartUi(state: Bundle?) {


    }

    override fun onStopUi() {
     }
}