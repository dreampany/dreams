package com.dreampany.crypto.ui.home.activity

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.crypto.databinding.RecyclerActivityBinding
import com.dreampany.crypto.ui.home.adapter.FastCoinAdapter
import com.dreampany.crypto.ui.home.vm.CoinViewModel
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.exts.init
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.toTint
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.ui.home.model.CoinItem
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FavoriteCoinsActivity : InjectActivity() {

    @Inject
    internal lateinit var cryptoPref: AppPref

    private lateinit var bind: RecyclerActivityBinding
    private lateinit var vm: CoinViewModel
    private lateinit var adapter: FastCoinAdapter

    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.recycler_activity

    override val toolbarId: Int = R.id.toolbar

    override val menuRes: Int = R.menu.menu_search

    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        onRefresh()
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

    private fun initUi() {
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
        if (!::adapter.isInitialized) {
            adapter = FastCoinAdapter(
                { currentPage ->
                    Timber.v("CurrentPage: %d", currentPage)
                    //onRefresh()
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

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<CoinItem>>) {
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
        /*showDialogue(
            R.string.title_dialog_features,
            message = error.message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )*/
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
            Type.COIN,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.VIEW,
            item.input
        )
        open(CoinActivity::class, task)
    }
}