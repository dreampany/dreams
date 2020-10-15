package com.dreampany.tools.ui.news.activity

import android.os.Bundle
import android.view.MenuItem
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.NewsActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.news.adapter.ArticlePagerAdapter
import com.dreampany.tools.ui.news.model.CategoryItem
import com.dreampany.tools.ui.news.vm.CategoryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

/**
 * Created by roman on 14/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NewsActivity : InjectActivity() {

    @Inject
    internal lateinit var ad: AdManager

    private lateinit var bind: NewsActivityBinding
    private lateinit var vm: CategoryViewModel
    private lateinit var adapter: ArticlePagerAdapter

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.news_activity
    override val menuRes: Int = R.menu.menu_news
    override val toolbarId: Int = R.id.toolbar
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, "NewsActivity")

            params.put(Constant.Event.activity(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        initPager()
        ad.loadBanner(this.javaClass.simpleName)
        ad.showInHouseAds(this)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorites -> {
                //openFavoritesUi()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //adapter.filter(newText)
        return false
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

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(CategoryViewModel::class)

        vm.subscribes(this, { this.processResponses(it) })

    }

    private fun initPager() {
        if (::adapter.isInitialized) return
        adapter = ArticlePagerAdapter(this)
        bind.layoutPager.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.layoutPager.pager,
            { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(result: List<CategoryItem>?) {
        if (result != null) {
            if (!adapter.isEmpty) {
                adapter.clear()
            }
            adapter.addItems(result)
        }
    }
}