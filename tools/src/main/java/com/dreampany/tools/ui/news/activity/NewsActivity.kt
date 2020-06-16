package com.dreampany.tools.ui.news.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dreampany.framework.misc.exts.toTint
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.NewsActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.news.adapter.ArticlePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.content_pager_ad.view.*
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
    private lateinit var adapter: ArticlePagerAdapter

    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.news_activity
    override val menuRes: Int = R.menu.menu_news

    override val toolbarId: Int = R.id.toolbar
    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        initPager()
        loadUi()
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

    override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(this, R.color.material_white)
        findMenuItemById(R.id.item_favorites).toTint(this, R.color.material_white)
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
        bind = getBinding()
    }

    private fun initPager() {
        adapter = ArticlePagerAdapter(this)
        bind.layoutPager.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.layoutPager.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
    }

    private fun loadUi() {
        adapter.addItems()
    }
}