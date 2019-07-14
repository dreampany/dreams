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

    init {
        KEY_NOTIFY_COIN = TextUtil.getString(context, R.string.key_notify_coin)!!
        KEY_NOTIFY_NEWS = TextUtil.getString(context, R.string.key_notify_news)!!
    }

    fun setVersionCode(versionCode: Int) {
        setPrivately(Constants.Pref.VERSION_CODE, versionCode)
    }

    fun getVersionCode(): Int {
        return getPrivately(Constants.Pref.VERSION_CODE, 0)
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
        setPrivately(Constants.Pref.LOADED, true)
    }

    fun isLoaded(): Boolean {
        return getPrivately(Constants.Pref.LOADED, false)
    }

    @Synchronized
    fun clearCoinIndexTime(source: String, currency: String, coinIndex: Int) {
        removePrivately(Constants.Pref.COIN_INDEX_TIME + source + currency + coinIndex)
    }

    @Synchronized
    fun commitCoinIndexTime(source: String, currency: String, coinIndex: Int) {
        setPrivately(Constants.Pref.COIN_INDEX_TIME + source + currency + coinIndex, TimeUtil.currentTime())
    }

    @Synchronized
    fun getCoinIndexTime(source: String, currency: String, coinIndex: Int): Long {
        return getPrivately(Constants.Pref.COIN_INDEX_TIME + source + currency + coinIndex, 0L)
    }

    @Synchronized
    fun commitCoinTime(source: String, currency: String, coinId: String) {
        commitCoinTime(source, currency, coinId, TimeUtil.currentTime())
    }

    @Synchronized
    fun commitCoinTime(source: String, currency: String, coinId: String, time: Long) {
        setPrivately(Constants.Pref.COIN_TIME + source + currency + coinId, time)
    }

    @Synchronized
    fun getCoinTime(source: String, currency: String, coinId: String): Long {
        return getPrivately(Constants.Pref.COIN_TIME + source + currency + coinId, 0L)
    }


    @Synchronized
    fun commitNewsTime() {
        setPrivately(Constants.Pref.NEWS_TIME, TimeUtil.currentTime())
    }

    @Synchronized
    fun getNewsTime(): Long {
        return getPrivately(Constants.Pref.NEWS_TIME, 0L)
    }

    @Synchronized
    fun commitIcoTime(status: IcoStatus) {
        setPrivately(Constants.Pref.ICO_TIME + status.name, TimeUtil.currentTime())
    }

    @Synchronized
    fun getIcoTime(status: IcoStatus): Long {
        return getPrivately(Constants.Pref.ICO_TIME + status.name, 0L)
    }


    @Synchronized
    fun setCurrency(currency: Currency) {
        setPrivately(Constants.Pref.CURRENCY, currency)
    }

    @Synchronized
    fun getCurrency(currency: Currency): Currency {
        return getPrivately(Constants.Pref.CURRENCY, Currency::class.java, currency)
    }

    @Synchronized
    fun setGraphCurrency(currency: Currency) {
        setPrivately(Constants.Pref.CURRENCY_GRAPH, currency)
    }

    @Synchronized
    fun getGraphCurrency(currency: Currency): Currency {
        return getPrivately(Constants.Pref.CURRENCY_GRAPH, Currency::class.java, currency)
    }

    @Synchronized
    fun commitGraphTime(symbol: String, currency: String) {
        setPrivately(Constants.Pref.GRAPH_SYMBOL + symbol + currency, TimeUtil.currentTime())
    }

    @Synchronized
    fun getGraphTime(symbol: String, currency: String): Long {
        return getPrivately(Constants.Pref.GRAPH_SYMBOL + symbol + currency, 0L)
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