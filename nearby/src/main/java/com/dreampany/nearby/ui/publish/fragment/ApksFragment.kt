package com.dreampany.nearby.ui.publish.fragment

import android.os.Bundle
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.nearby.databinding.RecyclerChildFragmentBinding
import javax.inject.Inject

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class ApksFragment @Inject constructor() : InjectFragment() {

    private lateinit var bind: RecyclerChildFragmentBinding

    override fun onStartUi(state: Bundle?) {


    }

    override fun onStopUi() {
    }

}