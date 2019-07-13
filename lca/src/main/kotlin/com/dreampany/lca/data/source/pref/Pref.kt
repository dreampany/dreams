package com.dreampany.lca.data.source.pref

import android.content.Context
import com.dreampany.frame.data.source.pref.BasePrefKt
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.lca.R
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.enums.IcoStatus
import com.dreampany.lca.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref @Inject constructor(val context: Context) : BasePrefKt(context) {
    private val KEY_NOTIFY_COIN: String
    private val KEY_NOTIFY_NEWS: String
    private val LOADED = "loaded"
    private val COIN_INDEX_TIME = "coin_index_time"
    private val COIN_TIME = "coin_time"
    private val ICO_TIME = "ico_time"
    private val NEWS_TIME = "news_time"
    private val CURRENCY = "currency"
    private val CURRENCY_GRAPH = "currency_graph"
    private val GRAPH_SYMBOL = "graph_symbol"

    init {
        KEY_NOTIFY_COIN = TextUtil.getString(context, R.string.key_notify_coin)!!
        KEY_NOTIFY_NEWS = TextUtil.getString(context, R.string.key_notify_news)!!
    }

    fun hasNotification(): Boolean {
        return hasNotifyCoin() || hasNotifyNews()
    }

    fun hasNotifyCoin(): Boolean {
        return getPublicly(KEY_NOTIFY_COIN, Boolean::class.java, true)
    }

    fun hasNotifyNews(): Boolean {
        return getPublicly(KEY_NOTIFY_NEWS, Boolean::class.java, true)
    }

    fun commitLoaded() {
        setPrivately(LOADED, true)
    }

    fun isLoaded(): Boolean {
        return getPrivately(LOADED, false)
    }

    @Synchronized
    fun clearCoinIndexTime(source: String, currency: String, coinIndex: Int) {
        removePrivately(COIN_INDEX_TIME + source + currency + coinIndex)
    }

    @Synchronized
    fun commitCoinIndexTime(source: String, currency: String, coinIndex: Int) {
        setPrivately(COIN_INDEX_TIME + source + currency + coinIndex, TimeUtil.currentTime())
    }

    @Synchronized
    fun getCoinIndexTime(source: String, currency: String, coinIndex: Int): Long {
        return getPrivately(COIN_INDEX_TIME + source + currency + coinIndex, 0L)
    }

    @Synchronized
    fun commitCoinTime(source: String, currency: String, coinId: String) {
        commitCoinTime(source, currency, coinId, TimeUtil.currentTime())
    }

    @Synchronized
    fun commitCoinTime(source: String, currency: String, coinId: String, time: Long) {
        setPrivately(COIN_TIME + source + currency + coinId, time)
    }

    @Synchronized
    fun getCoinTime(source: String, currency: String, coinId: String): Long {
        return getPrivately(COIN_TIME + source + currency + coinId, 0L)
    }


    @Synchronized
    fun commitNewsTime() {
        setPrivately(NEWS_TIME, TimeUtil.currentTime())
    }

    @Synchronized
    fun getNewsTime(): Long {
        return getPrivately(NEWS_TIME, 0L)
    }

    @Synchronized
    fun commitIcoTime(status: IcoStatus) {
        setPrivately(ICO_TIME + status.name, TimeUtil.currentTime())
    }

    @Synchronized
    fun getIcoTime(status: IcoStatus): Long {
        return getPrivately(ICO_TIME + status.name, 0L)
    }


    @Synchronized
    fun setCurrency(currency: Currency) {
        setPrivately(CURRENCY, currency)
    }

    @Synchronized
    fun getCurrency(currency: Currency): Currency {
        return getPrivately(CURRENCY, Currency::class.java, currency)
    }

    @Synchronized
    fun setGraphCurrency(currency: Currency) {
        setPrivately(CURRENCY_GRAPH, currency)
    }

    @Synchronized
    fun getGraphCurrency(currency: Currency): Currency {
        return getPrivately(CURRENCY_GRAPH, Currency::class.java, currency)
    }

    @Synchronized
    fun commitGraphTime(symbol: String, currency: String) {
        setPrivately(GRAPH_SYMBOL + symbol + currency, TimeUtil.currentTime())
    }

    @Synchronized
    fun getGraphTime(symbol: String, currency: String): Long {
        return getPrivately(GRAPH_SYMBOL + symbol + currency, 0L)
    }

    @Synchronized
    fun commitAlertProfitableCoin() {
        setPrivately(Constants.Pref.ALERT_PROFITABLE_COIN, TimeUtil.currentTime())
    }

    @Synchronized
    fun getAlertProfitableCoin(): Long {
        return getPrivately(Constants.Pref.ALERT_PROFITABLE_COIN, 0L)
    }

    @Synchronized
    fun commitAlertCoin() {
        setPrivately(Constants.Pref.ALERT_COIN, TimeUtil.currentTime())
    }

    @Synchronized
    fun getAlertCoin(): Long {
        return getPrivately(Constants.Pref.ALERT_COIN, 0L)
    }

    @Synchronized
    fun commitAlertNews() {
        setPrivately(Constants.Pref.ALERT_NEWS, TimeUtil.currentTime())
    }

    @Synchronized
    fun getAlertNews(): Long {
        return getPrivately(Constants.Pref.ALERT_NEWS, 0L)
    }
}