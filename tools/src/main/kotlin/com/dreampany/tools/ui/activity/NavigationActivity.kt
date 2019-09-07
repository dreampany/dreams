package com.dreampany.tools.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ActivityNavigationBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.fragment.HomeFragment
import com.dreampany.tools.ui.fragment.MoreFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.misc.SmartAd
import com.dreampany.framework.ui.activity.BaseBottomNavigationActivity
import com.dreampany.tools.data.misc.LoadRequest
import com.dreampany.tools.ui.vm.LoaderViewModel
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NavigationActivity : BaseBottomNavigationActivity() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var homeFragment: Lazy<HomeFragment>
    @Inject
    internal lateinit var moreFragment: Lazy<MoreFragment>
    @Inject
    internal lateinit var ad: SmartAd

    private lateinit var bind: ActivityNavigationBinding
    private lateinit var loaderVm: LoaderViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_navigation
    }

    override fun getToolbarId(): Int {
        return R.id.toolbar
    }

    override fun getNavigationViewId(): Int {
        return R.id.navigation_view
    }

    override fun getDefaultSelectedNavigationItemId(): Int {
        return R.id.item_home
    }

    override fun isHomeUp(): Boolean {
        return false
    }

    override fun hasDoubleBackPressed(): Boolean {
        return true
    }

    override fun getScreen(): String {
        return Constants.navigation(applicationContext)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        loadRequest()
        ad.loadBanner(getScreen())
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

    override fun onNavigationItem(navigationItemId: Int) {
        when (navigationItemId) {
            R.id.item_home -> commitFragment(HomeFragment::class.java, homeFragment, R.id.layout)
            R.id.item_more -> commitFragment(MoreFragment::class.java, moreFragment, R.id.layout)
        }
    }

    private fun initUi() {
        bind = super.binding as ActivityNavigationBinding
        val uiTask = getCurrentTask<UiTask<*>>(false)
        if (uiTask != null && uiTask.notify) {
            openActivity(ToolsActivity::class.java, uiTask)
            return
        }

        loaderVm = ViewModelProviders.of(this, factory).get(LoaderViewModel::class.java)

        ad.initAd(
            this,
            getScreen(),
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun loadRequest() {
        val request = LoadRequest(type = Type.WORD, action = Action.LOAD)
        loaderVm.request(request)
    }
}