package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
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
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.databinding.RecyclerActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.crypto.adapter.FastCoinAdapter
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import kotlinx.android.synthetic.main.content_recycler.view.*
import timber.log.Timber
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FavoriteCoinsActivity : InjectActivity() {

    @Inject
    internal lateinit var ad: AdManager

    @Inject
    internal lateinit var cryptoPref: CryptoPref

    private lateinit var bind: RecyclerActivityBinding
    private lateinit var vm: CoinViewModel
    private lateinit var adapter: FastCoinAdapter

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.recycler_activity
    override val toolbarId: Int = R.id.toolbar
    override val menuRes: Int = R.menu.menu_search
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constants.Param.PACKAGE_NAME, packageName)
            param.put(Constants.Param.VERSION_CODE, versionCode)
            param.put(Constants.Param.VERSION_NAME, versionName)
            param.put(Constants.Param.SCREEN, "FavoriteCoinsActivity")

            params.put(Constants.Event.ACTIVITY, param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        initRecycler(state)
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

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return false
    }

    override fun onRefresh() {
        loadCoins()
    }

    private fun loadCoins() {
        if (adapter.isEmpty)
            vm.loadFavoriteCoins()
        else
            bind.swipe.refresh(false)
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
        bind.swipe.init(this)
        bind.stateful.setStateView(
            StatefulLayout.State.EMPTY,
            R.layout.content_empty_favorite_coins
        )

        vm = createVm(CoinViewModel::class)
        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastCoinAdapter(
            { currentPage ->
                Timber.v("CurrentPage: %d", currentPage)
                //onRefresh()
            }, this::onItemPressed
        )
        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler,
            cryptoPref.getCurrency(),
            cryptoPref.getSort(),
            cryptoPref.getOrder()
        )
    }

    private fun processResponse(response: Response<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<CoinItem>>) {
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

    private fun processResults(result: List<CoinItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun onItemPressed(view: View, item: CoinItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openCoinUi(item)
            }
            R.id.button_favorite -> {

            }
            else -> {

            }
        }
    }

    private fun openCoinUi(item: CoinItem) {
        val task = UiTask(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.VIEW,
            item.input
        )
        open(CoinActivity::class, task)
    }
}