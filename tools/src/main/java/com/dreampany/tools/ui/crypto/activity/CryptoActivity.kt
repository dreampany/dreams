package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.extension.addDecoration
import com.dreampany.common.misc.func.OnVerticalScrollListener
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.databinding.CryptoActivityBinding
import com.dreampany.tools.misc.constant.AppConstants
import com.dreampany.tools.ui.crypto.adapter.CoinAdapter
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CryptoActivity : InjectActivity() , BaseAdapter.OnItemClickListener<Coin>{

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var bind: CryptoActivityBinding
    private lateinit var vm: CoinViewModel

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var coinAdapter: CoinAdapter

    override fun hasBinding(): Boolean = true

    override fun layoutId(): Int = R.layout.crypto_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        vm.loadCoins(coinAdapter.itemCount.toLong(), AppConstants.Limit.Crypto.LIST)
    }

    override fun onStopUi() {
    }

    override fun onItemClick(item: Coin) {
        openUi(item)
    }

    override fun onChildItemClick(view: View, item: Coin) {
    }

    private fun initUi() {
        bind = getBinding()
        vm = ViewModelProvider(this, factory).get(CoinViewModel::class.java)

        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler() {
        scroller = object : OnVerticalScrollListener() {
            override fun onScrolledToBottom() {

            }
        }
        coinAdapter = CoinAdapter(this)

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
        }
    }

    private fun processResponse(response: Response<List<Coin>, Type, Subtype>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<List<Coin>, Type, Subtype>) {
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
        coinAdapter.addAll(coins, true)
    }

    private fun openUi(item: Coin) {

    }
}