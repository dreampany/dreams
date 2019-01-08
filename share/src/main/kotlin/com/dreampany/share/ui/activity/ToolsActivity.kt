package com.dreampany.share.ui.activity

import android.os.Bundle
import com.dreampany.frame.misc.SmartAd
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.share.R
import com.dreampany.share.ui.enums.UiSubtype
import com.dreampany.share.ui.enums.UiType
import com.dreampany.share.ui.fragment.AboutFragment
import com.dreampany.share.ui.fragment.LicenseFragment
import com.dreampany.share.ui.fragment.MediaFragment
import com.dreampany.share.ui.fragment.SettingsFragment
import com.dreampany.share.ui.model.UiTask
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
    lateinit var mediaProvider: Lazy<MediaFragment>
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
        setTitle(null)
        val uiTask = getCurrentTask<UiTask<*>>(false) ?: return
        val type = uiTask.type
        val subtype = uiTask.subtype
        if (type == null || subtype == null) {
            return
        }
        ad.loadBanner(findViewById(R.id.adview))
        when (type) {
            UiType.MORE -> {
                when (subtype) {
                    UiSubtype.SETTINGS -> {
                        commitFragment(SettingsFragment::class.java, settingsProvider, R.id.layout, uiTask)
                    }
                    UiSubtype.ABOUT -> {
                        commitFragment(AboutFragment::class.java, aboutProvider, R.id.layout, uiTask)
                    }
                    else -> {
                    }
                }
            }
            UiType.MEDIA -> {
                when (subtype) {
                    UiSubtype.EDIT -> {
                        commitFragment(MediaFragment::class.java, mediaProvider, R.id.layout, uiTask)
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
