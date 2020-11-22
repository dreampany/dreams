package com.dreampany.tools.misc.func

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.color
import com.dreampany.framework.misc.exts.isEmpty
import com.dreampany.tools.R
import com.dreampany.tools.data.model.crypto.Currency
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

    private val formatRes: Int
    private val formatJoinRes: Int
    private val formats: MutableMap<String, String>

    //private val cryptos: Set<Currency>
    private val formatter: DecimalFormat

    private val THOUSAND = BigInteger.valueOf(1000)
    private val NAMES = arrayOf("T", "M", "B", "T")
    private val MAP: NavigableMap<BigInteger, String>
    private val symbols: MutableMap<Currency, String>

    @StringRes
    private val btcFormat: Int

    @StringRes
    private val positiveRatio: Int

    @StringRes
    private val negativeRatio: Int

    init {
        formatRes = R.string.currency_symbol_format
        formatJoinRes = R.string.currency_symbol_join_format

        formats = Maps.newConcurrentMap()
        // cryptos = Sets.newHashSet(*Currency.getCryptos())
        formatter = DecimalFormat(context.getString(R.string.crypto_formatter))
        symbols = Maps.newConcurrentMap()
        MAP = TreeMap()
        for (index in NAMES.indices) {
            MAP[THOUSAND.pow(index.inc())] = NAMES[index]
        }
        loadFormats()

        btcFormat = R.string.btc_format
        positiveRatio = R.string.positive_ratio_format
        negativeRatio = R.string.negative_ratio_format
    }

    @StringRes
    fun getRatio(value: Double): Int =
        if (value >= 0.0f) positiveRatio else negativeRatio

    @ColorInt
    fun getColor(value: Double): Int =
        context.color(if (value >= 0.0f) R.color.material_green700 else R.color.material_red700)

    private fun loadFormats() {
/*        formats[Currency.BTC.name] = context.getString(R.string.btc_format)
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
        formats[Currency.GBP.name] = getString(R.string.gbp_format)*/
        formats[Currency.USD.name] = getString(R.string.usd_format)
    }

    private fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun getCryptoString(price: Double): String {
        return formatter.format(price)
    }

    fun format(currency: Currency, price: Double): String {
        /*if (cryptos.contains(currency)) {
            val priceValue = getCryptoString(price)
            return String.format(formats.get(currency.name)!!, priceValue)
        }*/
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
        val entry = MAP.floorEntry(number) ?: return 0.toString()
        val key = entry.key
        val d = key.divide(THOUSAND)
        val m = number.divide(d)
        val f = m.toFloat() / 1000.0f
        val rounded = (f * 100.0).toInt() / 100.0f
        val result = StringBuilder(if ((rounded % 1).isEmpty) rounded.toInt().toString() else rounded.toString())
        result.append(Constant.Sep.SPACE).append(entry.value)
        return result.toString()
    }

    fun getSymbol(currency: Currency): String? {
        if (!symbols.containsKey(currency)) {
            var symbol: String? = null
            if (currency.isCrypto) {
                /*when (currency) {
                    Currency.BTC -> symbol = context.getString(R.string.btc_symbol)
                    Currency.ETH -> symbol = context.getString(R.string.eth_symbol)
                    Currency.LTC -> symbol = context.getString(R.string.ltc_symbol)
                }*/
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
        val amount = roundPrice(price)
        return formatPrice(amount, currency)
    }

    fun roundPrice(@StringRes formatRes: Int, price: Double, currency: Currency): String {
        val symbol = getSymbol(currency)
        val amount = roundPrice(price)
        //return symbol + Constants.Sep.SPACE + amount
        return context.getString(formatRes, symbol, amount)
    }

    fun formatPrice(price: Double, currency: Currency): String {
        return context.getString(formatRes, currency.sign, price)
    }

    fun formatPrice(price: String, currency: Currency): String {
        return context.getString(formatJoinRes, currency.sign, price)
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
        format.append(Constant.Sep.SPACE_HYPHEN_SPACE).append(priceValue)
        format.append(Constant.Sep.COMMA_SPACE).append(change)
        return format.toString()
    }

    fun getPriceResId(currency: Currency): Int {
        var resId = 0
        /*when (currency) {
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
        }*/
        return resId
    }

    private fun getText(@StringRes resId: Int): String? {
        return context.getString(resId)
    }

    private fun getText(@StringRes resId: Int, vararg args: Any): String? {
        return context.getString(resId, args)
    }
}