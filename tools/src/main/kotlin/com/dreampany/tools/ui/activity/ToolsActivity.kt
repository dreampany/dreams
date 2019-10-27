package com.dreampany.tools.ui.activity

import android.os.Bundle
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.misc.SmartAd
import com.dreampany.framework.ui.activity.BaseActivity
import com.dreampany.framework.ui.activity.WebActivity
import com.dreampany.framework.ui.callback.SearchViewCallback
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ActivityCollapseToolsBinding
import com.dreampany.tools.databinding.ActivityToolsBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.fragment.*
import com.dreampany.tools.ui.fragment.note.NoteFragment
import com.dreampany.tools.ui.fragment.note.FavoriteNotesFragment
import com.dreampany.tools.ui.fragment.note.NoteHomeFragment
import com.dreampany.tools.ui.fragment.radio.RadioHomeFragment
import com.dreampany.tools.ui.fragment.vpn.ServersFragment
import com.dreampany.tools.ui.fragment.vpn.VpnHomeFragment
import com.dreampany.tools.ui.fragment.word.*
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
    internal lateinit var ad: SmartAd
    @Inject
    internal lateinit var settingsProvider: Lazy<SettingsFragment>
    @Inject
    internal lateinit var licenseProvider: Lazy<LicenseFragment>

    @Inject
    internal lateinit var aboutProvider: Lazy<AboutFragment>
    @Inject
    internal lateinit var appHomeProvider: Lazy<AppHomeFragment>
    @Inject
    internal lateinit var noteHomeProvider: Lazy<NoteHomeFragment>
    @Inject
    internal lateinit var wordHomeProvider: Lazy<WordHomeFragment>
    @Inject
    internal lateinit var vpnHomeProvider: Lazy<VpnHomeFragment>
    @Inject
    internal lateinit var radioHomeProvider: Lazy<RadioHomeFragment>

    @Inject
    internal lateinit var favoriteNotesProvider: Lazy<FavoriteNotesFragment>
    @Inject
    internal lateinit var wordProvider: Lazy<WordFragment>
    @Inject
    internal lateinit var favoriteWordsProvider: Lazy<FavoriteWordsFragment>
    @Inject
    internal lateinit var wordVisionProvider: Lazy<WordVisionFragment>
    @Inject
    internal lateinit var wordQuizProvider: Lazy<WordQuizFragment>
    @Inject
    internal lateinit var relatedQuizProvider: Lazy<RelatedQuizFragment>
    @Inject
    internal lateinit var scanProvider: Lazy<ScanFragment>
    @Inject
    internal lateinit var noteProvider: Lazy<NoteFragment>
    @Inject
    internal lateinit var serversProvider: Lazy<ServersFragment>

    private lateinit var bind: ActivityToolsBinding
    private lateinit var collapseBind: ActivityCollapseToolsBinding

    override fun getLayoutId(): Int {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        if (uiTask != null && uiTask.collapseToolbar) {
            return R.layout.activity_collapse_tools
        }
        return R.layout.activity_tools
    }

    override fun isFullScreen(): Boolean {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        return uiTask?.fullscreen ?: super.isFullScreen()
    }

    override fun getScreen(): String {
        return Constants.tools(this)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
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
                when (state) {
                    State.SETTINGS -> {
                        commitFragment(
                            SettingsFragment::class.java,
                            settingsProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                    State.LICENSE -> {
                        commitFragment(
                            LicenseFragment::class.java,
                            licenseProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                    State.ABOUT -> {
                        commitFragment(
                            AboutFragment::class.java,
                            aboutProvider,
                            R.id.layout,
                            uiTask
                        )
                    }
                }
            }
            Type.SITE -> {
                if (action == Action.OPEN) {
                    openActivity(WebActivity::class.java, uiTask, true)
                }
            }
            Type.APP -> {
                if (state == State.HOME) {
                    commitFragment(
                        AppHomeFragment::class.java,
                        appHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                }
            }
            Type.VPN -> {
                if (state == State.HOME) {
                    commitFragment(
                        VpnHomeFragment::class.java,
                        vpnHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                }
            }
            Type.RADIO -> {
                if (state == State.HOME) {
                    commitFragment(
                        RadioHomeFragment::class.java,
                        radioHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                }
            }
            Type.NOTE -> {
                if (state == State.HOME) {
                    commitFragment(
                        NoteHomeFragment::class.java,
                        noteHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
                if (state == State.FAVORITE) {
                    commitFragment(
                        FavoriteNotesFragment::class.java,
                        favoriteNotesProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
                if (action == Action.VIEW || action == Action.ADD || action == Action.EDIT) {
                    commitFragment(
                        NoteFragment::class.java,
                        noteProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
            }
            Type.WORD -> {
                if (state == State.HOME) {
                    commitFragment(
                        WordHomeFragment::class.java,
                        wordHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
                if (state == State.FAVORITE) {
                    commitFragment(
                        FavoriteWordsFragment::class.java,
                        favoriteWordsProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
                if (action == Action.OPEN) {
                    commitFragment(
                        WordFragment::class.java,
                        wordProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
            }
            Type.QUIZ -> {
                when (state) {
                    State.HOME -> {
                        commitFragment(
                            WordQuizFragment::class.java,
                            wordQuizProvider,
                            R.id.layout,
                            uiTask
                        )
                        return
                    }
                }
                when (subtype) {
                    Subtype.SYNONYM,
                    Subtype.ANTONYM -> {
                        commitFragment(
                            RelatedQuizFragment::class.java,
                            relatedQuizProvider,
                            R.id.layout,
                            uiTask
                        )
                        return
                    }
                }

            }
            Type.OCR -> {
                if (action == Action.OPEN) {
                    commitFragment(
                        WordVisionFragment::class.java,
                        wordVisionProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
            }
            Type.SERVER -> {
                if (state == State.LIST) {
                    commitFragment(
                        ServersFragment::class.java,
                        serversProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
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
        val uiTask = getCurrentTask<UiTask<*>>(true)
        if (uiTask != null && uiTask.collapseToolbar) {
            return collapseBind.searchView
        }
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

    private fun initUi() {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        if (uiTask != null && uiTask.collapseToolbar) {
            collapseBind = super.binding as ActivityCollapseToolsBinding
        } else {
            bind = super.binding as ActivityToolsBinding
        }
    }
}
