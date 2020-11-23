package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.databinding.CoinActivityBinding
import com.dreampany.tools.manager.AdsManager
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.exts.setUrl
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.ui.crypto.adapter.PageAdapter
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinActivity : InjectActivity() {

    @Inject
    internal lateinit var ads: AdsManager

    @Inject
    internal lateinit var pref: Prefs

    @Inject
    internal lateinit var formatter: CurrencyFormatter

    private lateinit var bind: CoinActivityBinding
    private lateinit var adapter: PageAdapter
    private lateinit var vm: CoinViewModel
    private lateinit var input: Coin
    private lateinit var quote: Quote

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.coin_activity
    override val toolbarId: Int = R.id.toolbar

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, "CoinActivity")

            params.put(Constant.Event.activity(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return
        initUi()
        initPager()
        initAd()
        loadPager()
        ads.loadBanner(this.javaClass.simpleName)
    }

    override fun onStopUi() {
    }

    override fun onStart() {
        super.onStart()
        loadCoin()
    }

    override fun onResume() {
        super.onResume()
        ads.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ads.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(CoinViewModel::class)

        vm.subscribe(this, { this.processResponse(it) })

        bind.icon.setUrl(
            String.format(
                Locale.ENGLISH,
                Constants.Apis.CoinMarketCap.IMAGE_URL,
                input.id
            )
        )

        val title =
            String.format(
                Locale.ENGLISH,
                getString(R.string.crypto_name_symbol),
                input.name,
                input.symbol
            )

        bind.title.text = title


        //bind.subtitle.text = subtitle
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

    private fun initAd() {
        ads.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun loadPager() {
        adapter.addItems(input)
    }

    private fun loadUi() {
        val positiveRatio = R.string.positive_ratio_format
        val negativeRatio = R.string.negative_ratio_format


        val currency = pref.currency
        val price = quote.price
        val change24h = quote.getPercentChange24h()

        bind.price.text = formatter.formatPrice(price, currency)

        val change24hFormat = if (change24h.isPositive) positiveRatio else negativeRatio
        bind.change.text = formatString(change24hFormat, change24h)
        val change24hColor =
            if (change24h >= 0.0f) R.color.material_green700 else R.color.material_red700
        bind.change.setTextColor(color(change24hColor))
    }

    private fun loadCoin() {
        vm.loadCoin(input.id)
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, CoinItem>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            process(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, CoinItem>) {
            Timber.v("Result [%s]", response.result)
            process(response.result)
        }
    }

    private fun process(error: SmartError) {
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

    private fun process(result: CoinItem?) {
        if (result != null) {
            input = result.input.first
            quote = result.input.second
            adapter.update(input, quote)
        }
        loadUi()
    }
}