package com.dreampany.crypto.ui.home.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.crypto.databinding.RecyclerFragmentBinding
import com.dreampany.crypto.ui.home.activity.CoinActivity
import com.dreampany.crypto.ui.home.activity.FavoriteCoinsActivity
import com.dreampany.crypto.ui.home.adapter.FastCoinAdapter
import com.dreampany.crypto.ui.home.model.CoinItem
import com.dreampany.crypto.ui.home.vm.CoinViewModel
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import kotlinx.android.synthetic.main.content_recycler_ad.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var cryptoPref: AppPref

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: CoinViewModel
    private lateinit var adapter: FastCoinAdapter

    override val layoutRes: Int = R.layout.recycler_fragment
    override val menuRes: Int = R.menu.menu_home
    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        if (adapter.isEmpty)
            onRefresh()
    }

    override fun onStopUi() {
        //adapter.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(context, R.color.material_white)
        findMenuItemById(R.id.item_favorites).toTint(context, R.color.material_white)
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

    private fun initUi() {
        bind = getBinding()
        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_coins)
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

    private fun processResponse(response: Response<Type, Subtype, State, Action, CoinItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, CoinItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<CoinItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<CoinItem>>) {
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

    private fun processResult(result: CoinItem?) {
        if (result != null) {
            adapter.updateItem(result)
        }
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

    private fun onFavoriteClicked(item: CoinItem) {
        vm.toggleFavorite(item.input, CoinItem.ItemType.ITEM)
    }


    private fun openCoinUi(item: CoinItem) {
        val task = UiTask(
            Type.COIN,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.VIEW,
            item.input
        )
        open(CoinActivity::class, task)
    }

    private fun openFavoritesUi() {
        open(FavoriteCoinsActivity::class)
    }
}