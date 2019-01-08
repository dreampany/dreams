package com.dreampany.play2048.ui.activity

import android.os.Bundle
import android.view.MenuItem
import com.dreampany.frame.misc.SmartAd
import com.dreampany.frame.ui.activity.BaseMenuActivity
import com.dreampany.play2048.R
import com.dreampany.play2048.ui.fragment.MoreFragment
import com.dreampany.play2048.ui.fragment.PlayFragment
import dagger.Lazy
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class HomeActivity : BaseMenuActivity() {

    @Inject
    lateinit var playProvider: Lazy<PlayFragment>
    @Inject
    lateinit var moreProvider: Lazy<MoreFragment>
    @Inject
    lateinit var ad: SmartAd

    override fun getLayoutId(): Int {
        return R.layout.activity_tools
    }

    override fun getMenuId(): Int {
        return R.menu.menu_more
    }

    override fun isHomeUp(): Boolean {
        return false
    }

    override fun onStartUi(state: Bundle?) {
        //ad.loadBanner(findViewById(R.id.adview))
        ad.loadInterstitial(R.string.interstitial_ad_unit_id)
        commitFragment(PlayFragment::class.java, playProvider, R.id.layout)
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
        if (fragment !is PlayFragment) {
            commitFragment(PlayFragment::class.java, playProvider, R.id.layout)
            return
        }
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_more -> {
                commitFragment(MoreFragment::class.java, moreProvider, R.id.layout)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initView(state: Bundle?) {

/*        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        view.hasSaveState = settings.getBoolean("save_state", false)

        if (state != null) {
            if (state.getBoolean("hasState")) {
                //load()
            }
        }
        setContentView(view)*/
    }
}
