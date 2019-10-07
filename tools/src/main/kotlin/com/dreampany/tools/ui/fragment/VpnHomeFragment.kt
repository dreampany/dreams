package com.dreampany.tools.ui.fragment

import android.os.Bundle
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.tools.R
import javax.inject.Inject

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class VpnHomeFragment
@Inject constructor() : BaseMenuFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_vpn_home
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_vpn
    }

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {

    }
}