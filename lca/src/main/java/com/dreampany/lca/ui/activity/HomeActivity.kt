package com.dreampany.lca.ui.activity

import android.os.Bundle
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.ui.activity.InjectBottomNavigationActivity
import com.dreampany.lca.databinding.HomeActivityBinding
import com.dreampany.lca.manager.AdManager
import com.dreampany.lca.misc.Constants.Companion.navigation
import com.dreampany.lca.ui.fragment.*
import com.dreampany.lca.ui.model.UiTask
import dagger.Lazy
import com.dreampany.lca.R
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class HomeActivity : InjectBottomNavigationActivity() {

    @Inject
    internal lateinit var ad: AdManager

    @Inject
    internal lateinit var coins: Lazy<CoinsFragment>

    @Inject
    internal lateinit var library: Lazy<LibraryFragment>

    @Inject
    internal lateinit var ico: Lazy<IcoFragment>

    @Inject
    internal lateinit var newsFragment: Lazy<NewsFragment>

    @Inject
    internal lateinit var moreFragment: Lazy<MoreFragment>

    private lateinit var bind: HomeActivityBinding

    override val doubleBackPressed: Boolean = true

    override val layoutRes: Int = R.layout.home_activity

    override val toolbarId: Int = R.id.toolbar

    override val navigationViewId: Int = R.id.navigation_view

    override val selectedNavigationItemId: Int = R.id.item_coins

    val screen: String = navigation(this)

    override fun onStartUi(state: Bundle?) {
        initUi()
        initAd()
        ad.loadBanner(this.javaClass.simpleName)
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ad.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onNavigationItem(navigationItemId: Int) {
       /* when (navigationItemId) {
            R.id.navigation_home -> {
                setTitle(R.string.home)
                commitFragment(HomeFragment::class, home, R.id.layout)
            }
            R.id.navigation_more -> {
                setTitle(R.string.more)
                commitFragment(MoreFragment::class, more, R.id.layout)
            }
        }*/
    }

    private fun initUi() {
        bind = getBinding()
    }

    private fun initAd() {
        ad.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    /*protected fun onNavigationItem(navigationItemId: Int) {
        when (navigationItemId) {
            R.id.item_coins -> commitFragment(CoinsFragment::class.java, coinsFragment, R.id.layout)
            R.id.item_library -> commitFragment(
                LibraryFragment::class.java,
                libraryFragment,
                R.id.layout
            )
            R.id.item_ico -> commitFragment(IcoFragment::class.java, icoFragment, R.id.layout)
            R.id.item_news -> commitFragment(NewsFragment::class.java, newsFragment, R.id.layout)
            R.id.item_more -> commitFragment(
                MoreFragment::class.java,
                moreFragment,
                R.id.layout
            )
        }
    }
*/
/*    fun execute(t: Task<*>) {
        setSelectedItem(R.id.item_coins)
    }*/
}