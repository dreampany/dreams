package com.dreampany.tools.ui.radio.activity

import android.os.Bundle
import android.view.MenuItem
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.source.radio.pref.Prefs
import com.dreampany.tools.databinding.RadioActivityBinding
import com.dreampany.tools.manager.AdsManager
import com.dreampany.tools.ui.radio.adapter.PageAdapter
import com.dreampany.tools.ui.radio.model.PageItem
import com.dreampany.tools.ui.radio.vm.PageViewModel
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 14/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RadioActivity : InjectActivity() {

    @Inject
    internal lateinit var ads: AdsManager

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: RadioActivityBinding
    private lateinit var vm: PageViewModel
    private lateinit var adapter: PageAdapter

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.radio_activity
    override val toolbarId: Int = R.id.toolbar

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, "StationsActivity")

            params.put(Constant.Event.activity(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        initPager()
        ads.loadBanner(this.javaClass.simpleName)
        ads.showInHouseAds(this)

        if (pref.isPagesSelected.not()) {
            open(PagesActivity::class)
        }
    }

    override fun onStopUi() {
    }

    override fun onStart() {
        super.onStart()
        updatePages()
    }

    override fun onResume() {
        super.onResume()
        ads.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ads.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorites -> {
                //openFavoritesUi()
                return true
            }
            R.id.item_settings -> {
                openSettingsUi()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAd() {
        ads.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        vm = createVm(PageViewModel::class)

        vm.subscribes(this, { this.processResponses(it) })
    }

    private fun initPager() {
        if (::adapter.isInitialized) return
        adapter = PageAdapter(this)
        bind.layoutPager.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.layoutPager.pager,
            { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<PageItem>>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<PageItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
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

    private fun processResults(result: List<PageItem>?) {
        if (result != null) {
            if (!adapter.isEmpty) {
                adapter.clear()
            }
            adapter.addItems(result)
        }
    }

    private fun updatePages() {
        val pages = pref.pages ?: return
        if (adapter.hasUpdate(pages)) {
            vm.readsCache()
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    private fun openSettingsUi() {
        open(SettingsActivity::class)
    }
}