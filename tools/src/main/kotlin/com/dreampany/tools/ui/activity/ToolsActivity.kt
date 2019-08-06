package com.dreampany.tools.ui.activity

import android.os.Bundle
import com.dreampany.frame.misc.SmartAd
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.tools.R
import com.dreampany.tools.ui.enums.UiSubtype
import com.dreampany.tools.ui.enums.UiType
import com.dreampany.tools.ui.fragment.*
import com.dreampany.tools.ui.model.UiTask
import com.google.android.gms.ads.AdView
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class ToolsActivity : BaseActivity() {

    @Inject
    lateinit var ad: SmartAd
    @Inject
    lateinit var settingsProvider: Lazy<SettingsFragment>
    @Inject
    lateinit var licenseProvider: Lazy<LicenseFragment>
    @Inject
    lateinit var aboutProvider: Lazy<AboutFragment>
    @Inject
    lateinit var apkProvider: Lazy<ApkFragment>
    @Inject
    lateinit var scanProvider: Lazy<ScanFragment>
    @Inject
    lateinit var notesProvider: Lazy<NotesFragment>
    @Inject
    lateinit var editNoteProvider: Lazy<EditNoteFragment>

    override fun getLayoutId(): Int {
        return R.layout.activity_tools
    }

    override fun isFullScreen(): Boolean {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        return uiTask?.fullscreen ?: super.isFullScreen()
    }

    override fun onStartUi(state: Bundle?) {
        val uiTask = getCurrentTask<UiTask<*>>(false) ?: return
        val type = uiTask.type
        val subtype = uiTask.subtype
        if (type == null || subtype == null) {
            return
        }
        ad.initAd(
            this,
            getScreen(),
            findViewById<AdView>(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
        ad.loadAd(getScreen())

        when (type) {
            UiType.MORE -> {
                when (subtype) {
                    UiSubtype.SETTINGS -> {
                        commitFragment(
                            SettingsFragment::class.java,
                            settingsProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                    UiSubtype.LICENSE -> {
                        commitFragment(
                            LicenseFragment::class.java,
                            licenseProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                    UiSubtype.ABOUT -> {
                        commitFragment(
                            AboutFragment::class.java,
                            aboutProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                    else -> {
                    }
                }
            }
            UiType.APK -> {
                when (subtype) {
                    UiSubtype.HOME -> {
                        commitFragment(ApkFragment::class.java, apkProvider, R.id.layout, uiTask)
                    }
                }
            }
            UiType.SCAN -> {
                when (subtype) {
                    UiSubtype.HOME -> {
                        commitFragment(ScanFragment::class.java, scanProvider, R.id.layout, uiTask)
                    }
                }
            }
            UiType.NOTE -> {
                when (subtype) {
                    UiSubtype.HOME -> {
                        commitFragment(
                            NotesFragment::class.java,
                            notesProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                    UiSubtype.ADD,
                    UiSubtype.EDIT -> {
                        commitFragment(
                            EditNoteFragment::class.java,
                            editNoteProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun onStopUi() {
        ad.destroyBanner(getScreen())
    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(getScreen())
    }

    override fun onPause() {
        ad.pauseBanner(getScreen())
        super.onPause()
    }

/*    override fun onDestroy() {
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
    }*/
}
