package com.dreampany.crypto.misc.func

import android.content.Context
import androidx.annotation.StringRes
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Currency
import com.dreampany.framework.misc.constant.Constants
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CurrencyFormatter
@Inject constructor(
    private val context: Context
) {

    private val formats: MutableMap<String, String>
    private val cryptos: Set<Currency>
    private val formatter: DecimalFormat

    private val THOUSAND = BigInteger.valueOf(1000)
    private val NAMES = arrayOf("T", "M", "B", "T")
    private val MAP: NavigableMap<BigInteger, String>
    private val symbols: MutableMap<Currency, String>

    init {
        formats = Maps.newConcurrentMap()
        cryptos = Sets.newHashSet(*Currency.getCryptos())
        formatter = DecimalFormat(context.getString(R.string.crypto_formatter))
        symbols = Maps.newConcurrentMap()
        MAP = TreeMap()
        for (i in NAMES.indices) {
            MAP[THOUSAND.pow(i + 1)] = NAMES[i]
        }
        loadFormats()
    }

    private fun loadFormats() {
        formats[Currency.BTC.name] = context.getString(R.string.btc_format)
        formats[Currency.ETH.name] = context.getString(R.string.eth_format)
        formats[Currency.LTC.name] = context.getString(R.string.ltc_format)

        formats[Currency.AUD.name] = getString(R.string.aud_format)
        formats[Currency.BRL.name] = getString(R.string.brl_format)
        formats[Currency.CAD.name] = getString(R.string.usd_format)
        formats[Currency.CHF.name] = getString(R.string.chf_format)
        formats[Currency.CLP.name] = getString(R.string.usd_format)
        formats[Currency.CNY.name] = getString(R.string.cny_format)
        formats[Currency.CZK.name] = getString(R.string.czk_format)
        formats[Currency.DKK.name] = getString(R.string.dkk_format)
        formats[Currency.GBP.name] = getString(R.string.gbp_format)
        formats[Currency.USD.name] = getString(R.string.usd_format)
    }

    private fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun getCryptoString(price: Double): String {
        return formatter.format(price)
    }

    fun format(currency: Currency, price: Double): String {
        if (cryptos.contains(currency)) {
            val priceValue = getCryptoString(price)
            return String.format(formats.get(currency.name)!!, priceValue)
        }
        val format = formats.get(currency.name)
        if (format != null) {
            return String.format(format, price)
        }
        val nf = NumberFormat.getInstance(context.resources.configuration.locale)
        nf.maximumFractionDigits = 10
        return currency.name + " " + nf.format(price)
    }

    fun format(currencyValue: String, price: Double): String {
        val nf = NumberFormat.getInstance(context.resources.configuration.locale)
        nf.maximumFractionDigits = 10
        return currencyValue + " " + nf.format(price)
    }

    fun roundPrice(price: Double): String {
        val number = BigDecimal.valueOf(price).toBigInteger()
        //BigInteger number = new BigInteger(String.valueOf(price));
        val entry = MAP.floorEntry(number) ?: return "0"
        val key = entry.key
        val d = key.divide(THOUSAND)
        val m = number.divide(d)
        val f = m.toFloat() / 1000.0f
        val rounded = (f * 100.0).toInt() / 100.0f
        return if (rounded % 1 == 0f) {
            rounded.toInt().toString() + " " + entry.value
        } else rounded.toString() + " " + entry.value
    }

    fun getSymbol(currency: Currency): String? {
        if (!symbols.containsKey(currency)) {
            var symbol: String? = null
            if (currency.isCrypto) {
                when (currency) {
                    Currency.BTC -> symbol = context.getString(R.string.btc_symbol)
                    Currency.ETH -> symbol = context.getString(R.string.eth_symbol)
                    Currency.LTC -> symbol = context.getString(R.string.ltc_symbol)
                }
            } else {
                symbol = java.util.Currency.getInstance(currency.name).symbol
            }
            symbol?.run {
                symbols[currency] = this
            }
        }
        return symbols[currency]
    }

    fun roundPrice(price: Double, currency: Currency): String {
        val symbol = getSymbol(currency)
        val amount = roundPrice(price)
        return symbol + Constants.Sep.SPACE + amount
    }

    fun formatPrice(price: Double, currency: Currency): String? {
        val priceResId = getPriceResId(currency)
        return context.getString(priceResId, price)
    }

    fun formatPrice(
        symbol: String,
        name: String,
        price: Double,
        dayChange: Double,
        currency: Currency
    ): String {
        val coin = context.getString(R.string.crypto_symbol_name, symbol, name)
        val priceValue = getText(R.string.with_price, formatPrice(price, currency)!!)
        val change =
            getText(R.string.with_change, getText(R.string.positive_ratio_format, dayChange) as Any)
        val format = StringBuilder(coin)
        format.append(Constants.Sep.SPACE_HYPHEN_SPACE).append(priceValue)
        format.append(Constants.Sep.COMMA_SPACE).append(change)
        return format.toString()
    }

    fun getPriceResId(currency: Currency): Int {
        var resId = 0
        when (currency) {
            Currency.BRL -> resId = R.string.brl_format
            Currency.CAD, Currency.HKD, Currency.SEK, Currency.USD -> resId = R.string.usd_format
            Currency.CHF -> resId = R.string.chf_format
            Currency.CNY -> resId = R.string.cny_format
            Currency.EUR -> resId = R.string.euro_format
            Currency.INR -> resId = R.string.inr_format
            Currency.ILS -> resId = R.string.ils_format
            Currency.JPY -> resId = R.string.jpy_format
            Currency.KRW -> resId = R.string.krw_format
            Currency.TRY -> resId = R.string.try_format
            Currency.ZAR -> resId = R.string.zar_format
        }
        return resId
    }

    private fun getText(@StringRes resId: Int): String? {
        return context.getString(resId)
    }

    private fun getText(@StringRes resId: Int, vararg args: Any): String? {
        return context.getString(resId, args)
    }
}