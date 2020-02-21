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
import com.dreampany.tools.ui.fragment.AboutFragment
import com.dreampany.tools.ui.fragment.LicenseFragment
import com.dreampany.tools.ui.fragment.SettingsFragment
import com.dreampany.tools.ui.fragment.app.AppHomeFragment
import com.dreampany.tools.ui.fragment.block.BlockHomeFragment
import com.dreampany.tools.ui.fragment.crypto.CryptoHomeFragment
import com.dreampany.tools.ui.fragment.note.FavoriteNotesFragment
import com.dreampany.tools.ui.fragment.note.NoteFragment
import com.dreampany.tools.ui.fragment.note.NoteHomeFragment
import com.dreampany.tools.ui.fragment.question.QuestionHomeFragment
import com.dreampany.tools.ui.fragment.question.QuestionsFragment
import com.dreampany.tools.ui.fragment.radio.FavoriteStationsFragment
import com.dreampany.tools.ui.fragment.radio.RadioHomeFragment
import com.dreampany.tools.ui.fragment.resume.ResumeFragment
import com.dreampany.tools.ui.fragment.resume.ResumeHomeFragment
import com.dreampany.tools.ui.fragment.resume.ResumePreviewFragment
import com.dreampany.tools.ui.fragment.vpn.CountriesFragment
import com.dreampany.tools.ui.fragment.vpn.FavoriteServersFragment
import com.dreampany.tools.ui.fragment.vpn.ServersFragment
import com.dreampany.tools.ui.fragment.vpn.VpnHomeFragment
import com.dreampany.tools.ui.fragment.word.*
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.android.gms.ads.AdView
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by roman on 2020-02-20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ToolsActivity : BaseActivity(), SearchViewCallback {

    @Inject
    internal lateinit var ad: SmartAd
    @Inject
    internal lateinit var settingsProvider: Lazy<SettingsFragment>
    @Inject
    internal lateinit var licenseProvider: Lazy<LicenseFragment>

    /* home */
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
    internal lateinit var cryptoHomeProvider: Lazy<CryptoHomeFragment>
    @Inject
    internal lateinit var blockHomeProvider: Lazy<BlockHomeFragment>
    @Inject
    internal lateinit var resumeHomeProvider: Lazy<ResumeHomeFragment>



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

    /* radio */
    @Inject
    internal lateinit var favoriteStationsProvider: Lazy<FavoriteStationsFragment>

    /* resume */
    @Inject
    internal lateinit var resumeProvider: Lazy<ResumeFragment>
    @Inject
    internal lateinit var resumePreviewProvider: Lazy<ResumePreviewFragment>

    /* vpn */
    @Inject
    internal lateinit var serversProvider: Lazy<ServersFragment>
    @Inject
    internal lateinit var countriesProvider: Lazy<CountriesFragment>
    @Inject
    internal lateinit var favoriteServersProvider: Lazy<FavoriteServersFragment>

    /* question */
    @Inject
    internal lateinit var questionHomeProvider: Lazy<QuestionHomeFragment>
    internal lateinit var questionsProvider: Lazy<QuestionsFragment>

    private lateinit var bind: ActivityToolsBinding
    private lateinit var collapseBind: ActivityCollapseToolsBinding

    override fun getLayoutId(): Int {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        if (uiTask != null && uiTask.collapseToolbar) {
            return R.layout.activity_collapse_tools
        }
        return R.layout.activity_tools
    }

    override fun isPortrait(): Boolean {
        return true
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
                    return
                }
                if (state == State.FAVORITE) {
                    commitFragment(
                        FavoriteServersFragment::class.java,
                        favoriteServersProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
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
                    return
                }
                if (state == State.FAVORITE) {
                    commitFragment(
                        FavoriteStationsFragment::class.java,
                        favoriteStationsProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
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
            Type.COUNTRY -> {
                if (state == State.LIST) {
                    commitFragment(
                        CountriesFragment::class.java,
                        countriesProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
            }
            Type.CRYPTO -> {
                if (subtype == Subtype.DEFAULT) {
                    commitFragment(
                        CryptoHomeFragment::class.java,
                        cryptoHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
            }
            Type.BLOCK -> {
                if (subtype == Subtype.DEFAULT) {
                    commitFragment(
                        BlockHomeFragment::class.java,
                        blockHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
            }
            Type.RESUME -> {
                if (state == State.HOME) {
                    commitFragment(
                        ResumeHomeFragment::class.java,
                        resumeHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
                when (action) {
                    Action.ADD,
                    Action.EDIT -> {
                        commitFragment(
                            ResumeFragment::class.java,
                            resumeProvider,
                            R.id.layout,
                            uiTask
                        )
                        return
                    }
                    Action.PREVIEW -> {
                        commitFragment(
                            ResumePreviewFragment::class.java,
                            resumePreviewProvider,
                            R.id.layout,
                            uiTask
                        )
                        return
                    }
                }
            }
            Type.QUESTION -> {
                if (state == State.HOME) {
                    commitFragment(
                        QuestionHomeFragment::class.java,
                        questionHomeProvider,
                        R.id.layout,
                        uiTask
                    )
                    return
                }
                if (action == Action.SOLVE) {
                    commitFragment(
                        QuestionsFragment::class.java,
                        questionsProvider,
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

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }*/

    override fun getSearchView(): SimpleSearchView {
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
