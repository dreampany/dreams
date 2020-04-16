package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.extension.toTint
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.home.Subtype
import com.dreampany.tools.data.enums.home.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.databinding.CoinsActivityBinding
import com.dreampany.tools.misc.CurrencyFormatter
import com.dreampany.tools.misc.constant.AppConstants
import com.dreampany.tools.ui.crypto.adapter.FastCoinAdapter
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
    internal lateinit var factory: ViewModelProvider.Factory

    @Inject
    internal lateinit var cryptoPref: CryptoPref

    @Inject
    internal lateinit var formatter: CurrencyFormatter

    private lateinit var bind: CoinsActivityBinding
    private lateinit var vm: CoinViewModel

    //private lateinit var scroller: OnVerticalScrollListener
    //private lateinit var coinAdapter: CoinAdapter
    private lateinit var adapter: FastCoinAdapter

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.coins_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun menuRes(): Int = R.menu.menu_coins

    override fun searchMenuItemId(): Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        vm.loadCoins(adapter.itemCount, AppConstants.Limit.Crypto.LIST)
    }

    override fun onStopUi() {
        adapter.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(this, R.color.material_white)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //coinAdapter.getFilter().filter(newText)
        adapter.filter(newText)
        return false
    }

    /*override fun onItemClick(item: Coin) {
        openUi(item)
    }

    override fun onChildItemClick(view: View, item: Coin) {
    }*/

    private fun initUi() {
        bind = getBinding()
        vm = ViewModelProvider(this, factory).get(CoinViewModel::class.java)

        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastCoinAdapter()
        }
        adapter.initRecycler(
            state, bind.recycler, formatter, cryptoPref.getCurrency(),
            cryptoPref.getSort(),
            cryptoPref.getOrder()
        )
        //val fastScrollIndicatorAdapter = FastScrollIndicatorAdapter<ModelIconItem>()
        /*itemAdapter = ModelAdapter { element: Coin ->
            CoinItem(
                element,
                formatter,
                cryptoPref.getCurrency(),
                cryptoPref.getSort(),
                cryptoPref.getOrder()
            )
        }
        itemAdapter.itemFilter.filterPredicate = { item: CoinItem, constraint: CharSequence? ->
            item.coin.name.toString().contains(constraint.toString(), ignoreCase = true)
        }*/

        /*fastAdapter = FastAdapter.with(listOf(itemAdapter))
        bind.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fastAdapter
            addItemDecoration(
                ItemSpaceDecoration(
                    context.dimension(R.dimen.recycler_horizontal_spacing).toInt(),
                    context.dimension(R.dimen.recycler_vertical_spacing).toInt(),
                    1,
                    true
                )
            )
        }
        fastAdapter.withSavedInstanceState(state)*/

        /*scroller = object : OnVerticalScrollListener() {
            override fun onScrolledToBottom() {

            }
        }
        coinAdapter = CoinAdapter(this)
        coinAdapter.setProperty(cryptoPref.getCurrency(), cryptoPref.getSort(), cryptoPref.getOrder(),formatter)

        val recyclerLayout = LinearLayoutManager(this)
        recyclerLayout.orientation = RecyclerView.VERTICAL
        recyclerLayout.isSmoothScrollbarEnabled = true

        bind.recycler.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            itemAnimator = DefaultItemAnimator()
            addOnScrollListener(scroller)
            adapter = coinAdapter
            addDecoration(4)
        }*/
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<Coin>>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<Coin>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: Throwable) {
        showDialogue(
            R.string.title_dialog_features,
            message = error.message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(coins: List<Coin>) {

    }

    private fun openUi(item: Coin) {

    }
}