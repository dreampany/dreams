package com.dreampany.tools.ui.wifi.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.enums.wifi.WifiAction
import com.dreampany.tools.data.enums.wifi.WifiState
import com.dreampany.tools.data.enums.wifi.WifiSubtype
import com.dreampany.tools.data.enums.wifi.WifiType
import com.dreampany.tools.data.source.wifi.pref.WifiPref
import com.dreampany.tools.databinding.RecyclerActivityAdBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.dreampany.tools.ui.wifi.adapter.FastWifiAdapter
import com.dreampany.tools.ui.wifi.model.WifiItem
import com.dreampany.tools.ui.wifi.vm.WifiViewModel
import kotlinx.android.synthetic.main.content_recycler_ad.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifisActivity : InjectActivity() {

    @Inject
    internal lateinit var ad: AdManager

    @Inject
    internal lateinit var wifiPref: WifiPref

    private lateinit var bind: RecyclerActivityAdBinding
    private lateinit var adapter: FastWifiAdapter
    private lateinit var vm: WifiViewModel

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.recycler_activity_ad
    override val menuRes: Int = R.menu.menu_search
    override val toolbarId: Int = R.id.toolbar
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constants.Param.PACKAGE_NAME, packageName)
            param.put(Constants.Param.VERSION_CODE, versionCode)
            param.put(Constants.Param.VERSION_NAME, versionName)
            param.put(Constants.Param.SCREEN, "WifisActivity")

            params.put(Constants.Event.ACTIVITY, param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        initRecycler(state)
        onRefresh()
        ad.loadBanner(this.javaClass.simpleName)
        ad.showInHouseAds(this)
    }

    override fun onStopUi() {
        adapter.destroy()
        vm.stopPeriodicWifis()
    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ad.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorites -> {
                openFavoritesUi()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return false
    }

    override fun onRefresh() {
        loadWifis()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onItemPressed(view: View, item: WifiItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                // openCoinUi(item)
            }
            R.id.button_favorite -> {
                //onFavoriteClicked(item)
            }
            else -> {

            }
        }
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
        if (!::bind.isInitialized) {
            bind = getBinding()
            bind.swipe.init(this)
            bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_wifis)
            vm = createVm(WifiViewModel::class)
            vm.subscribes(this, Observer { this.processResponses(it) })
        }
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastWifiAdapter(
                { currentPage ->
                    Timber.v("CurrentPage: %d", currentPage)
                    //onRefresh()
                }, this::onItemPressed
            )
            adapter.initRecycler(state, bind.layoutRecycler.recycler)
        }
    }

    private fun loadWifis() {
        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            vm.loadWifis(adapter.itemCount.toLong(), {
                if (isMinQ) {
                    open(Settings.Panel.ACTION_WIFI, 0)
                }
            })
            // TODO work for ACTION_WIFI in periodic
            vm.startPeriodicWifis()
        }
    }

    private fun processResponse(response: Response<WifiType, WifiSubtype, WifiState, WifiAction, WifiItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<WifiType, WifiSubtype, WifiState, WifiAction, WifiItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processResponses(response: Response<WifiType, WifiSubtype, WifiState, WifiAction, List<WifiItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<WifiType, WifiSubtype, WifiState, WifiAction, List<WifiItem>>) {
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

    private fun processResult(result: WifiItem?) {
        if (result != null) {
            adapter.updateItem(result)
        }
    }

    private fun processResults(result: List<WifiItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun onFavoriteClicked(item: CoinItem) {
        //vm.toggleFavorite(item.item, CoinItem.ItemType.ITEM)
    }


    private fun openCoinUi(item: CoinItem) {
        val task = UiTask(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.VIEW,
            item.item
        )
        //open(CoinActivity::class, task)
    }

    private fun openFavoritesUi() {
        //open(FavoriteCoinsActivity::class)
    }
}