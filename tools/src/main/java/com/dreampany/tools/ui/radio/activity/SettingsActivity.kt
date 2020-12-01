package com.dreampany.tools.ui.radio.activity

import android.os.Bundle
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.SettingsActivityBinding
import com.dreampany.tools.manager.AdsManager
import com.dreampany.tools.ui.radio.fragment.SettingsFragment
import dagger.Lazy
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by roman on 8/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SettingsActivity : InjectActivity() {

    @Inject
    internal lateinit var ads: AdsManager

    @Inject
    internal lateinit var settings: Lazy<SettingsFragment>

    private lateinit var bind: SettingsActivityBinding

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.settings_activity
    override val toolbarId: Int = R.id.toolbar

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, Constant.Param.screen(this))

            params.put(Constant.Event.key(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        commitFragment(SettingsFragment::class, settings, R.id.layout)
    }
}