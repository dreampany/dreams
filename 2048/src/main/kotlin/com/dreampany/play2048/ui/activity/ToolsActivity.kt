package com.dreampany.play2048.ui.activity

import android.os.Bundle
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.play2048.R
import com.dreampany.play2048.ui.enums.UiSubtype
import com.dreampany.play2048.ui.enums.UiType
import com.dreampany.play2048.ui.fragment.AboutFragment
import com.dreampany.play2048.ui.fragment.LicenseFragment
import com.dreampany.play2048.ui.fragment.SettingsFragment
import com.dreampany.play2048.ui.model.UiTask
import com.dreampany.frame.misc.SmartAd
import dagger.Lazy
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class ToolsActivity : BaseActivity() {

    @Inject
    lateinit var settingsProvider: Lazy<SettingsFragment>
    @Inject
    lateinit var licenseProvider: Lazy<LicenseFragment>
    @Inject
    lateinit var aboutProvider: Lazy<AboutFragment>
    @Inject
    lateinit var ad: SmartAd

    override fun getLayoutId(): Int {
        return R.layout.activity_tools
    }

    override fun isFullScreen(): Boolean {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        return uiTask?.isFullscreen ?: super.isFullScreen()
    }

    override fun onStartUi(state: Bundle?) {
        val uiTask = getCurrentTask<UiTask<*>>(false) ?: return
        val type = uiTask.type
        val subtype = uiTask.subtype
        if (type == null || subtype == null) {
            return
        }
        ad.loadBanner(findViewById(R.id.adview))
        ad.loadInterstitial(R.string.debug_interstitial_ad_unit_id)
        when (type) {
            UiType.MORE -> {
                when (subtype) {
                    UiSubtype.SETTINGS -> {
                        commitFragment(SettingsFragment::class.java, settingsProvider, R.id.layout, uiTask)
                    }
                    UiSubtype.LICENSE -> {
                        commitFragment(LicenseFragment::class.java, licenseProvider, R.id.layout, uiTask)
                    }
                    UiSubtype.ABOUT -> {
                        commitFragment(AboutFragment::class.java, aboutProvider, R.id.layout, uiTask)
                    }
                    else -> {
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun onStopUi() {
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
        } catch (e: Exception) {
            Timber.e(e)
            getApp().getAnalytics().logEvent(e.toString(), getBundle())
        }
    }

    override fun onBackPressed() {
        val fragment = currentFragment
        if (fragment != null && fragment.hasBackPressed()) {
            return
        }
        finish()
    }
}
