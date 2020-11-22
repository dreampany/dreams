package com.dreampany.tools.ui.crypto.model

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.dreampany.framework.misc.exts.*
import com.dreampany.tools.R
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.databinding.CoinItemBinding
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.exts.setUrl
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import java.util.*

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinItem
constructor(
    val input: Pair<Coin, Quote>,
    val currency: Currency,
    val formatter: CurrencyFormatter,
    var favorite: Boolean
) : ModelAbstractBindingItem<Pair<Coin, Quote>, CoinItemBinding>(input) {

/*    companion object {
        fun getItem(
            item: Coin,
            formatter: CurrencyFormatter,
            currency: Currency,
            sort: String,
            order: String,
            favorite: Boolean = false
        ): CoinItem = CoinItem(ItemType.ITEM, item, formatter, currency, sort, order, favorite)

        fun getInfoItem(
            item: Coin,
            formatter: CurrencyFormatter,
            currency: Currency,
            sort: String,
            order: String,
            favorite: Boolean = false
        ): CoinItem = CoinItem(ItemType.INFO, item, formatter, currency, sort, order, favorite)

        fun getQuoteItem(
            item: Coin,
            formatter: CurrencyFormatter,
            currency: Currency,
            sort: String,
            order: String,
            favorite: Boolean = false
        ): CoinItem = CoinItem(ItemType.QUOTE, item, formatter, currency, sort, order, favorite)

    }*/

    @StringRes
    private val btcFormat: Int

    @StringRes
    private val positiveRatio: Int

    @StringRes
    private val negativeRatio: Int

    init {
        btcFormat = R.string.btc_format
        positiveRatio = R.string.positive_ratio_format
        negativeRatio = R.string.negative_ratio_format
    }

    override fun hashCode(): Int = Objects.hashCode(input.first.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as CoinItem
        return Objects.equal(this.input.first.id, item.input.first.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_coin_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): CoinItemBinding =
        CoinItemBinding.inflate(inflater, parent, false)


    override fun bindView(binding: CoinItemBinding, payloads: List<Any>) {
        val coin = input.first
        val quote = input.second

        binding.rank.text = coin.rank.toString()
        binding.layoutSimple.icon.setUrl(
            String.format(
                Locale.ENGLISH,
                Constants.Apis.CoinMarketCap.IMAGE_URL,
                coin.id
            )
        )

        val name =
            String.format(
                Locale.ENGLISH,
                binding.context.getString(R.string.crypto_symbol_name),
                coin.symbol,
                coin.name
            )
        binding.layoutSimple.textName.text = name

        val price = quote.price
        val change1h = quote.getPercentChange1h()
        val change24h = quote.getPercentChange24h()
        val change7d = quote.getPercentChange7d()
        val marketCap = quote.getMarketCap()
        val volume24h = quote.getVolume24h()

        binding.layoutSimple.textPrice.text = formatter.formatPrice(price, currency)
        binding.layoutPrice.textMarketCap.text = formatter.roundPrice(marketCap, currency)
        binding.layoutPrice.textVolume24h.text = formatter.roundPrice(volume24h, currency)

        val change1hFormat = if (change1h.isPositive) positiveRatio else negativeRatio
        val change24hFormat = if (change24h.isPositive) positiveRatio else negativeRatio
        val change7dFormat = if (change7d.isPositive) positiveRatio else negativeRatio

        //binding.layoutPrice.textChange1h.text = bind.context.formatString(change1hFormat, change1h)
        binding.layoutPrice.textChange24h.text = binding.context.formatString(change24hFormat, change24h)
        //binding.layoutPrice.textChange7d.text = bind.context.formatString(change7dFormat, change7d)

        val startColor = R.color.material_grey400
        val endColor =
            if (change1h.isPositive || change24h.isPositive || change7d.isPositive) R.color.material_green700 else R.color.material_red700

        binding.layoutSimple.textPrice.blink(startColor, endColor)

        val hourChangeColor =
            if (change1h >= 0.0f) R.color.material_green700 else R.color.material_red700
        //bind.layoutPrice.textChange1h.setTextColor(bind.color(hourChangeColor))

        val dayChangeColor =
            if (change24h >= 0.0f) R.color.material_green700 else R.color.material_red700
        binding.layoutPrice.textChange24h.setTextColor(binding.color(dayChangeColor))

        val weekChangeColor =
            if (change7d >= 0.0f) R.color.material_green700 else R.color.material_red700
        // bind.layoutPrice.textChange7d.setTextColor(bind.color(weekChangeColor))

        val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
            coin.getLastUpdated(),
            currentMillis,
            DateUtils.MINUTE_IN_MILLIS
        ) as String
        //bind.layoutSimple.textLastUpdated.text = lastUpdatedTime

        //bind.layoutOptions.buttonFavorite.isLiked = favorite
    }

    override fun unbindView(binding: CoinItemBinding) {

    }

    /*private fun bindItem(binding: CoinItemBinding) {

    }

    private fun bindItem(bind: CoinInfoItemBinding) {

        val quote = input.getQuote(currency)
        var change1h = 0.0
        var change24h = 0.0
        var change7d = 0.0
        var marketCap = 0.0
        var volume24h = 0.0
        if (quote != null) {
            change1h = quote.getChange1h()
            change24h = quote.getChange24h()
            change7d = quote.getChange7d()
            marketCap = quote.getMarketCap()
            volume24h = quote.getVolume24h()
        }

        val change1hFormat = if (change1h >= 0.0f) positiveRatio else negativeRatio
        val change24hFormat = if (change24h >= 0.0f) positiveRatio else negativeRatio
        val change7dFormat = if (change7d >= 0.0f) positiveRatio else negativeRatio

        bind.textChange1h.text =
            bind.context.formatString(change1hFormat, change1h)
        bind.textChange24h.text =
            bind.context.formatString(change24hFormat, change24h)
        bind.textChange7d.text =
            bind.context.formatString(change7dFormat, change7d)

        val hourChangeColor =
            if (change1h >= 0.0f) R.color.material_green700 else R.color.material_red700
        bind.textChange1h.setTextColor(bind.color(hourChangeColor))

        val dayChangeColor =
            if (change24h >= 0.0f) R.color.material_green700 else R.color.material_red700
        bind.textChange24h.setTextColor(bind.color(dayChangeColor))

        val weekChangeColor =
            if (change7d >= 0.0f) R.color.material_green700 else R.color.material_red700
        bind.textChange7d.setTextColor(bind.color(weekChangeColor))

        val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
            input.getLastUpdated(),
            Util.currentMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ) as String

        bind.layoutMarketCap.title.setText(R.string.market_cap)
        bind.layoutMarketCap.value.text = formatter.formatPrice(marketCap, currency)

        bind.layoutVolume.title.setText(R.string.volume_24h)
        bind.layoutVolume.value.text = formatter.formatPrice(volume24h, currency)

        //bind.layoutSimple.textLastUpdated.text = lastUpdatedTime
        //bind.buttonFavorite.isLiked = favorite
    }

    private fun bindItem(bind: CoinQuoteItemBinding) {
        val symbol = input.symbol
        val circulating = bind.context.getString(
            R.string.join_text,
            formatter.roundPrice(input.getCirculatingSupply()),
            symbol
        )

        val total = bind.context.getString(
            R.string.join_text,
            formatter.roundPrice(input.getTotalSupply()),
            symbol
        )
        val max = bind.context.getString(
            R.string.join_text,
            formatter.roundPrice(input.getMaxSupply()),
            symbol
        )

        bind.layoutCirculating.title.setText(R.string.circulating_supply)
        bind.layoutTotal.title.setText(R.string.total_supply)
        bind.layoutMax.title.setText(R.string.max_supply)

        bind.layoutCirculating.value.text = circulating
        bind.layoutTotal.value.text = total
        bind.layoutMax.value.text = max

        val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
            input.getLastUpdated(),
            Util.currentMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ) as String

        //bind.textLastUpdated.text = lastUpdatedTime
    }*/
}