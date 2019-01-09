package com.dreampany.radio.ui.activity

import android.os.Bundle
import android.view.MenuItem
import com.dreampany.frame.misc.SmartAd
import com.dreampany.frame.ui.activity.BaseMenuActivity
import com.dreampany.radio.R
import com.dreampany.radio.ui.fragment.MoreFragment
import com.dreampany.radio.ui.fragment.StationFragment
import dagger.Lazy
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by Hawladar Roman on 1/8/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class HomeActivity : BaseMenuActivity() {

    @Inject
    lateinit var stationProvider: Lazy<StationFragment>
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
        ad.loadInterstitial(R.string.debug_interstitial_ad_unit_id)
        //commitFragment(StationFragment::class.java, stationProvider, R.id.layout)
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
        if (fragment !is StationFragment) {
            commitFragment(StationFragment::class.java, stationProvider, R.id.layout)
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
}