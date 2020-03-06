package com.dreampany.tools.ui.fragment.lock

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.LayoutRes
import com.dreampany.common.misc.extension.hasUsagePermission
import com.dreampany.framework.api.service.ServiceManager
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.lockui.ui.activity.PinActivity
import com.dreampany.tools.R
import com.dreampany.tools.data.source.pref.LockPref
import com.dreampany.tools.service.AppService
import timber.log.Timber
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

    private val REQUEST_CODE_USAGE = 101
    private val REQUEST_CODE_LOCK = 102

    @Inject
    internal lateinit var lockPref: LockPref

    @Inject
    internal lateinit var service: ServiceManager

    @LayoutRes
    override fun getLayoutId(): Int = R.layout.fragment_lock_home

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_USAGE -> {
                Timber.v("Result Code %d", resultCode)
                requestLockUi()
            }
            REQUEST_CODE_LOCK -> {
                if (resultCode != PinActivity.RESULT_BACK_PRESSED) {
                    lockPref.commitPasscode()
                    lockPref.commitServicePermitted()
                    context?.run {
                        service.openService(AppService.getStartLockIntent(this))
                    }
                    loadUi()
                }
            }
        }
    }

    private fun initUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && context.hasUsagePermission()
                .not()
        ) {
            startActivityForResult(
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                REQUEST_CODE_USAGE
            )
        } else {
            requestLockUi()
        }
    }

    private fun loadUi() {
        //TODO load all apps
    }

    private fun requestLockUi() {
        startActivityForResult(
            PinActivity.getIntent(context!!, !lockPref.hasPasscode()),
            REQUEST_CODE_LOCK
        )
    }
}