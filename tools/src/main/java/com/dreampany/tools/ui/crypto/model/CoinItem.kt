package com.dreampany.tools.ui.crypto.model

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.extension.blink
import com.dreampany.common.misc.extension.formatString
import com.dreampany.common.misc.extension.gone
import com.dreampany.common.misc.extension.toColor
import com.dreampany.common.misc.util.Util
import com.dreampany.tools.R
import com.dreampany.tools.misc.constant.CryptoConstants
import com.dreampany.tools.data.enums.crypto.CoinSort
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.databinding.CoinItemBinding
import com.dreampany.tools.misc.CurrencyFormatter
import com.dreampany.tools.misc.extension.setUrl
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import java.util.*

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinItem(
    val item: Coin,
    val formatter: CurrencyFormatter,
    val currency: Currency,
    val sort: CoinSort,
    val order: Order
    ) : ModelAbstractBindingItem<Coin, CoinItemBinding>(item) {

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

    override fun hashCode(): Int = Objects.hashCode(item.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as CoinItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override val type: Int
        get() = R.id.adapter_coin_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): CoinItemBinding =
        CoinItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: CoinItemBinding, payloads: List<Any>) {
        bind.layoutSimple.imageIcon.setUrl(
            String.format(
                Locale.ENGLISH,
                CryptoConstants.CoinMarketCap.IMAGE_URL,
                item.id
            )
        )

        bind.layoutOptions.buttonFavorite.gone()

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

        bind.layoutPrice.textChange1h.text = bind.root.context.formatString(change1hFormat, change1h)
        bind.layoutPrice.textChange24h.text = bind.root.context.formatString(change24hFormat, change24h)
        bind.layoutPrice.textChange7d.text = bind.root.context.formatString(change7dFormat, change7d)

        val startColor = R.color.material_grey400
        val endColor =
            if (change1h >= 0.0f || change24h >= 0.0f || change7d >= 0.0f) R.color.material_green700 else R.color.material_red700

        bind.layoutSimple.textPrice.blink(startColor, endColor)

        val hourChangeColor =
            if (change1h >= 0.0f) R.color.material_green700 else R.color.material_red700
        bind.layoutPrice.textChange1h.setTextColor(hourChangeColor.toColor(bind.root.context))

        val dayChangeColor =
            if (change24h >= 0.0f) R.color.material_green700 else R.color.material_red700
        bind.layoutPrice.textChange24h.setTextColor(dayChangeColor.toColor(bind.root.context))

        val weekChangeColor =
            if (change7d >= 0.0f) R.color.material_green700 else R.color.material_red700
        bind.layoutPrice.textChange7d.setTextColor(weekChangeColor.toColor(bind.root.context))

        val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
            item.getLastUpdated(),
            Util.currentMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ) as String
        bind.layoutSimple.textLastUpdated.text = lastUpdatedTime
    }

    override fun unbindView(binding: CoinItemBinding) {

    }
}