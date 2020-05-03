package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.extension.init
import com.dreampany.framework.misc.extension.open
import com.dreampany.framework.misc.extension.refresh
import com.dreampany.framework.misc.extension.toTint
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.databinding.CoinsActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.crypto.adapter.FastCoinAdapter
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinsActivity : InjectActivity() {
    @Inject
    internal lateinit var ad: AdManager

    @Inject
    internal lateinit var cryptoPref: CryptoPref

    private lateinit var bind: CoinsActivityBinding
    private lateinit var vm: CoinViewModel
    private lateinit var adapter: FastCoinAdapter

    override fun homeUp(): Boolean = true

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.coins_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun menuRes(): Int = R.menu.menu_coins

    override fun searchMenuItemId(): Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        initAd()
        onRefresh()
        ad.loadBanner(this.javaClass.simpleName)
    }

    override fun onStopUi() {
        adapter.destroy()
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

    override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(this, R.color.material_white)
        findMenuItemById(R.id.item_favorites).toTint(this, R.color.material_white)
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
        loadCoins()
    }

    private fun onItemPressed(view: View, item: CoinItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openCoinUi(item)
            }
            R.id.button_favorite -> {
                onFavoriteClicked(item)
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
        bind = getBinding()
        bind.swipe.init(this)
        vm = createVm(CoinViewModel::class)
        vm.subscribe(this, Observer { this.processResponse(it) })
        vm.subscribes(this, Observer { this.processResponses(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastCoinAdapter(
                { currentPage ->
                    Timber.v("CurrentPage: %d", currentPage)
                    onRefresh()
                }, this::onItemPressed
            )
        }

        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler,
            cryptoPref.getCurrency(),
            cryptoPref.getSort(),
            cryptoPref.getOrder()
        )
    }

    private fun loadCoins() {
        vm.loadCoins(adapter.itemCount.toLong())
    }

    private fun processResponse(response: Response<CryptoType, CryptoSubtype, CryptoState, CryptoAction, CoinItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<CryptoType, CryptoSubtype, CryptoState, CryptoAction, CoinItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processResponses(response: Response<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<CoinItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<CoinItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message = if (error.hostError) getString(R.string.message_no_internet) else error.message
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

    private fun processResult(result: CoinItem?) {
        if (result == null) {

        } else {
            adapter.updateItem(result)
        }
    }

    private fun processResults(result: List<CoinItem>?) {
        if (result == null) {

        } else {
            adapter.addItems(result)
        }
    }

    private fun onFavoriteClicked(item: CoinItem) {
        vm.toggleFavorite(item.item, CoinItem.ItemType.ITEM)
    }


    private fun openCoinUi(item: CoinItem) {
        val task = UiTask(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.VIEW,
            item.item
        )
        open(CoinActivity::class, task)
    }

    private fun openFavoritesUi() {
        open(FavoriteCoinsActivity::class)
    }
}