package com.dreampany.tools.ui.activity

import android.os.Bundle
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.misc.SmartAd
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.frame.ui.callback.SearchViewCallback
import com.dreampany.tools.R
import com.dreampany.tools.ui.fragment.*
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.tools.databinding.ActivityToolsBinding
import com.dreampany.tools.databinding.FragmentWordHomeBinding
import com.google.android.gms.ads.AdView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class ToolsActivity : BaseActivity(), SearchViewCallback {

    @Inject
    lateinit var ad: SmartAd
    @Inject
    lateinit var settingsProvider: Lazy<SettingsFragment>
    @Inject
    lateinit var licenseProvider: Lazy<LicenseFragment>
    @Inject
    lateinit var aboutProvider: Lazy<AboutFragment>
    @Inject
    lateinit var appHomeProvider: Lazy<AppHomeFragment>
    @Inject
    lateinit var noteHomeProvider: Lazy<NoteHomeFragment>
    @Inject
    lateinit var wordHomeProvider: Lazy<WordHomeFragment>
    @Inject
    lateinit var scanProvider: Lazy<ScanFragment>
    @Inject
    lateinit var editNoteProvider: Lazy<EditNoteFragment>

    private lateinit var bind: ActivityToolsBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_tools
    }

    override fun isFullScreen(): Boolean {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        return uiTask?.fullscreen ?: super.isFullScreen()
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        val uiTask = getCurrentTask<UiTask<*>>(false) ?: return
        val type = uiTask.type
        val subtype = uiTask.subtype
        val state = uiTask.state
        val action = uiTask.action
        ad.initAd(
            this,
            getScreen(),
            findViewById<AdView>(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
        ad.loadAd(getScreen())

        when (type) {
            Type.MORE -> {
                when (subtype) {
                    Subtype.SETTINGS -> {
                        commitFragment(
                            SettingsFragment::class.java,
                            settingsProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                    Subtype.LICENSE -> {
                        commitFragment(
                            LicenseFragment::class.java,
                            licenseProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                    Subtype.ABOUT -> {
                        commitFragment(
                            AboutFragment::class.java,
                            aboutProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                }
            }
            Type.APP -> {
                if (subtype == Subtype.DEFAULT) {
                    if (state == State.HOME) {
                        commitFragment(
                            AppHomeFragment::class.java,
                            appHomeProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                }
            }
            Type.NOTE -> {
                if (subtype == Subtype.DEFAULT) {
                    if (state == State.HOME) {
                        if (action == Action.OPEN) {
                            commitFragment(
                                AppHomeFragment::class.java,
                                appHomeProvider,
                                R.id.layout,
                                uiTask
                            )
                        }
                    } else if (state == State.DEFAULT) {
                        if (action == Action.ADD || action == Action.EDIT) {
                            commitFragment(
                                EditNoteFragment::class.java,
                                editNoteProvider,
                                R.id.layout,
                                uiTask
                            )
                        }
                    }
                }
            }
            Type.WORD -> {
                if (subtype == Subtype.DEFAULT) {
                    if (state == State.HOME) {
                        commitFragment(
                            WordHomeFragment::class.java,
                            wordHomeProvider,
                            R.id.layout,
                            uiTask
                        )
                    } else if (state == State.DEFAULT) {
                        if (action == Action.ADD || action == Action.EDIT) {
                            /*commitFragment(
                                EditNoteFragment::class.java,
                                editNoteProvider,
                                R.id.layout,
                                uiTask
                            )*/
                        }
                    }
                }
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

    override fun getSearchView(): MaterialSearchView {
        return bind.searchView
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

    private fun initView() {
        bind = super.binding as ActivityToolsBinding
    }
}
