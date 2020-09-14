package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.databinding.CoinActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.misc.constants.CryptoConstants
import com.dreampany.tools.misc.exts.setUrl
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.ui.crypto.adapter.CoinPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.content_pager_ad.view.*
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
    internal lateinit var ad: AdManager

    @Inject
    internal lateinit var pref: CryptoPref

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
            param.put(Constants.Param.PACKAGE_NAME, packageName)
            param.put(Constants.Param.VERSION_CODE, versionCode)
            param.put(Constants.Param.VERSION_NAME, versionName)
            param.put(Constants.Param.SCREEN, "CoinActivity")

            params.put(Constants.Event.ACTIVITY, param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        val task: UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Coin> =
            (task ?: return) as UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Coin>
        input = task.input ?: return
        initUi()
        initPager()
        initAd()
        //setTitle(input.name)
        loadUi()
        ad.loadBanner(this.javaClass.simpleName)
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ad.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    private fun initUi() {
        bind = getBinding()
        bind.icon.setUrl(
            String.format(
                Locale.ENGLISH,
                CryptoConstants.CoinMarketCap.IMAGE_URL,
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

        val currency = pref.getCurrency()
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
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
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

    private fun loadUi() {
        adapter.addItems(input)
    }
}