package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.databinding.CoinActivityBinding
import com.dreampany.tools.manager.AdsManager
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.constants.CryptoConstants
import com.dreampany.tools.misc.exts.setUrl
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.ui.crypto.adapter.CoinPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
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
    private lateinit var adapter: CoinPagerAdapter

    private lateinit var input: Coin

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

            params.put(Constant.Event.ACTIVITY, param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        val task =
            (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return
        initUi()
        initPager()
        initAd()
        //setTitle(input.name)
        loadUi()
        ads.loadBanner(this.javaClass.simpleName)
    }

    override fun onStopUi() {
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
                getString(R.string.crypto_symbol_name),
                input.symbol,
                input.name
            )

        val currency = pref.currency
        val quote = input.getQuote(currency)
        val price = quote?.price.value
        val subtitle = formatter.formatPrice(price, currency)

        bind.title.text = title
        bind.subtitle.text = subtitle
    }

    private fun initPager() {
        adapter = CoinPagerAdapter(this)
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

    private fun loadUi() {
        adapter.addItems(input)
    }
}