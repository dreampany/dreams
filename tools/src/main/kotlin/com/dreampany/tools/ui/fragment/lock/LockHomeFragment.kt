package com.dreampany.tools.ui.fragment.lock

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.lockui.ui.activity.PinActivity
import com.dreampany.tools.R
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

    private val REQUEST_CODE = 101

    @Inject
    internal lateinit var lockPref: LockPref

    @LayoutRes
    override fun getLayoutId(): Int = R.layout.fragment_lock_home

    override fun onStartUi(state: Bundle?) {
        initUi()
        startActivityForResult(
            PinActivity.getIntent(context!!, !lockPref.hasPasscode()),
            REQUEST_CODE
        )
    }

    override fun onStopUi() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> {
                if (resultCode != PinActivity.RESULT_BACK_PRESSED) {
                    lockPref.commitPasscode()
                    loadUi()
                }
            }
        }
    }

    private fun initUi() {

    }

    private fun loadUi() {

    }
}