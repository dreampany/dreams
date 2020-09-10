package com.dreampany.tools.ui.crypto.model

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.dreampany.framework.data.enums.Order
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.R
import com.dreampany.tools.misc.exts.setUrl
import com.dreampany.tools.misc.constants.CryptoConstants
import com.dreampany.tools.data.enums.crypto.CoinSort
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.databinding.CoinInfoItemBinding
import com.dreampany.tools.databinding.CoinItemBinding
import com.dreampany.tools.databinding.CoinQuoteItemBinding
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import kotlinx.android.synthetic.main.coin_pair_item.view.*
import java.util.*

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinItem
private constructor(
    val itemType: ItemType,
    val item: Coin,
    val formatter: CurrencyFormatter,
    val currency: Currency,
    val sort: CoinSort,
    val order: Order,
    var favorite: Boolean
) : ModelAbstractBindingItem<Coin, ViewBinding>(item) {

    enum class ItemType {
        ITEM, INFO, QUOTE
    }

    companion object {
        fun getItem(
            item: Coin,
            formatter: CurrencyFormatter,
            currency: Currency,
            sort: CoinSort,
            order: Order,
            favorite: Boolean = false
        ): CoinItem = CoinItem(ItemType.ITEM, item, formatter, currency, sort, order, favorite)

        fun getInfoItem(
            item: Coin,
            formatter: CurrencyFormatter,
            currency: Currency,
            sort: CoinSort,
            order: Order,
            favorite: Boolean = false
        ): CoinItem = CoinItem(ItemType.INFO, item, formatter, currency, sort, order, favorite)

        fun getQuoteItem(
            item: Coin,
            formatter: CurrencyFormatter,
            currency: Currency,
            sort: CoinSort,
            order: Order,
            favorite: Boolean = false
        ): CoinItem = CoinItem(ItemType.QUOTE, item, formatter, currency, sort, order, favorite)

    }

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

    override fun hashCode(): Int = Objects.hashCode(itemType, item.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as CoinItem
        return item.itemType == itemType && Objects.equal(this.item.id, item.item.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() {
            when (itemType) {
                ItemType.ITEM -> return R.id.adapter_coin_item_id
                ItemType.INFO -> return R.id.adapter_coin_info_item_id
                ItemType.QUOTE -> return R.id.adapter_coin_quote_item_id
            }
        }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ViewBinding {
        when (itemType) {
            ItemType.ITEM -> return CoinItemBinding.inflate(inflater, parent, false)
            ItemType.INFO -> return CoinInfoItemBinding.inflate(inflater, parent, false)
            ItemType.QUOTE -> return CoinQuoteItemBinding.inflate(inflater, parent, false)
        }
    }


    override fun bindView(bind: ViewBinding, payloads: List<Any>) {
        if (bind is CoinItemBinding) {
            bindItem(bind)
        } else if (bind is CoinInfoItemBinding) {
            bindItem(bind)
        } else if (bind is CoinQuoteItemBinding) {
            bindItem(bind)
        }
    }

    override fun unbindView(binding: ViewBinding) {

    }

    private fun bindItem(bind: CoinItemBinding) {
        bind.layoutSimple.icon.setUrl(
            String.format(
                Locale.ENGLISH,
                CryptoConstants.CoinMarketCap.IMAGE_URL,
                item.id
            )
        )

        val nameText =
            String.format(
                Locale.ENGLISH,
                bind.root.context.getString(R.string.crypto_symbol_name),
                item.symbol,
                item.name
            )
        bind.layoutSimple.textName.text = nameText

        val quote = item.getQuote(currency)

        var price = 0.0
        var change1h = 0.0
        var change24h = 0.0
        var change7d = 0.0
        var marketCap = 0.0
        var volume24h = 0.0
        if (quote != null) {
            price = quote.price
            change1h = quote.getChange1h()
            change24h = quote.getChange24h()
            change7d = quote.getChange7d()
            marketCap = quote.getMarketCap()
            volume24h = quote.getVolume24h()
        }

        bind.layoutSimple.textPrice.text = formatter.formatPrice(price, currency)
        bind.layoutPrice.textMarketCap.text = formatter.roundPrice(marketCap, currency)
        bind.layoutPrice.textVolume24h.text = formatter.roundPrice(volume24h, currency)

        val change1hFormat = if (change1h >= 0.0f) positiveRatio else negativeRatio
        val change24hFormat = if (change24h >= 0.0f) positiveRatio else negativeRatio
        val change7dFormat = if (change7d >= 0.0f) positiveRatio else negativeRatio

        // bind.layoutPrice.textChange1h.text = bind.context.formatString(change1hFormat, change1h)
        bind.layoutPrice.textChange24h.text =
            bind.context.formatString(change24hFormat, change24h)
        // bind.layoutPrice.textChange7d.text = bind.context.formatString(change7dFormat, change7d)

        val startColor = R.color.material_grey400
        val endColor =
            if (change1h >= 0.0f || change24h >= 0.0f || change7d >= 0.0f) R.color.material_green700 else R.color.material_red700

        bind.layoutSimple.textPrice.blink(startColor, endColor)

        val hourChangeColor =
            if (change1h >= 0.0f) R.color.material_green700 else R.color.material_red700
        //bind.layoutPrice.textChange1h.setTextColor(bind.color(hourChangeColor))

        val dayChangeColor =
            if (change24h >= 0.0f) R.color.material_green700 else R.color.material_red700
        bind.layoutPrice.textChange24h.setTextColor(bind.color(dayChangeColor))

        val weekChangeColor =
            if (change7d >= 0.0f) R.color.material_green700 else R.color.material_red700
        // bind.layoutPrice.textChange7d.setTextColor(bind.color(weekChangeColor))

        val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
            item.getLastUpdated(),
            Util.currentMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ) as String
        //bind.layoutSimple.textLastUpdated.text = lastUpdatedTime

        //bind.layoutOptions.buttonFavorite.isLiked = favorite
    }

    private fun bindItem(bind: CoinInfoItemBinding) {

        val quote = item.getQuote(currency)
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
            item.getLastUpdated(),
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
        val symbol = item.symbol
        val circulating = bind.context.getString(
            R.string.join_text,
            formatter.roundPrice(item.getCirculatingSupply()),
            symbol
        )

        val total = bind.context.getString(
            R.string.join_text,
            formatter.roundPrice(item.getTotalSupply()),
            symbol
        )
        val max = bind.context.getString(
            R.string.join_text,
            formatter.roundPrice(item.getMaxSupply()),
            symbol
        )

        bind.layoutCirculating.title.setText(R.string.circulating_supply)
        bind.layoutTotal.title.setText(R.string.total_supply)
        bind.layoutMax.title.setText(R.string.max_supply)

        bind.layoutCirculating.value.text = circulating
        bind.layoutTotal.value.text = total
        bind.layoutMax.value.text = max

        val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
            item.getLastUpdated(),
            Util.currentMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ) as String

        //bind.textLastUpdated.text = lastUpdatedTime
    }
}