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
import com.dreampany.tools.databinding.ActivityToolsBinding
import com.dreampany.tools.ui.fragment.*
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
    lateinit var favoriteNotesProvider: Lazy<FavoriteNotesFragment>
    @Inject
    lateinit var wordHomeProvider: Lazy<WordHomeFragment>
    @Inject
    lateinit var wordProvider: Lazy<WordFragment>
    @Inject
    lateinit var favoriteWordsProvider: Lazy<FavoriteWordsFragment>
    @Inject
    lateinit var wordVisionProvider: Lazy<WordVisionFragment>
    @Inject
    lateinit var wordQuizProvider: Lazy<WordQuizFragment>
    @Inject
    lateinit var relatedQuizProvider: Lazy<RelatedQuizFragment>
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
                if (action == Action.ADD || action == Action.EDIT) {
                    commitFragment(
                        EditNoteFragment::class.java,
                        editNoteProvider,
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
