package com.dreampany.tools.ui.fragment.lock

import android.content.Intent
import android.os.Bundle
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.lockui.ui.activity.PinActivity
import com.dreampany.tools.data.source.pref.LockPref
import javax.inject.Inject

/**
 * Created by roman on 1/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class LockHomeFragment
@Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var lockPref: LockPref

    override fun onStartUi(state: Bundle?) {

        val intent = Intent(getParent(), PinActivity::class.java)
        startActivity(intent)

    }

    override fun onStopUi() {

    }
}