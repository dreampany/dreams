/*
package com.dreampany.lca.ui.model;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.api.cmc.model.CmcQuote;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.adapter.CoinDetailAdapter;
import com.dreampany.lca.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

*/
/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

public class CoinDetailItem extends BaseItem<Coin, CoinDetailItem.ViewHolder> {

    @NonNull
    private CoinDetailType type;

    private CoinDetailItem(@NonNull Coin coin, @NonNull CoinDetailType type, @LayoutRes int layoutId) {
        super(coin, layoutId);
        this.type = type;
    }

    public static CoinDetailItem getDetailHeaderItem(@NonNull Coin coin) {
        return new CoinDetailItem(coin, CoinDetailType.HEADER, R.layout.item_coin_details);
    }

    public static CoinDetailItem getDetailsItem(@NonNull Coin coin, @NonNull CoinDetailType type) {
        return new CoinDetailItem(coin, type, R.layout.item_coin_quote);
    }

    @Override
    public int getItemViewType() {
        return type.ordinal();
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        CoinDetailItem item = (CoinDetailItem) inObject;
        return Objects.equal(item.type, this.type);
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        switch (type) {
            case HEADER:
                holder.bindHeader(item);
                break;
            default:
                holder.bindItem(item, type);
                break;
        }
    }

    @Override
    public boolean filter(Serializable constraint) {
        return false;
    }


    static final class ViewHolder extends SmartAdapter.SmartViewHolder {

        CoinDetailAdapter adapter;
        //header and item - two types
        SimpleDraweeView icon;
        TextView name;
        TextView price;
        TextView time;
        TextView itemValue;

        String usdFormat;
        String btcFormat;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.adapter = (CoinDetailAdapter) adapter;
            itemValue = view.findViewById(R.id.text_value);
            name = view.findViewById(R.id.text_name);
            price = view.findViewById(R.id.text_price);
            time = view.findViewById(R.id.text_time);

            usdFormat = getText(R.string.usd_format);
            btcFormat = getText(R.string.btc_format);
        }

        void bindHeader(Coin coin) {
            String iconUrl = String.format(Constants.ImageUrl.INSTANCE.getCoinMarketCapImageUrl(), coin.getId());
            FrescoUtil.loadImage(icon, iconUrl);
            name.setText(coin.getName());

            CmcQuote priceQuote = coin.getUsdPriceQuote();
            price.setText(String.format(usdFormat, priceQuote.getPrice()));
            time.setText(TimeUtil.getUtcTime(coin.getLastUpdated()));
        }

        void bindItem(Coin coin, CoinDetailType type) {
            switch (type) {
                case SYMBOL:
                    String value = getItemText(R.string.symbol, coin.getSymbol());
                    name.setText(value);
                    break;
                case PRICE_USD:
                    CmcQuote priceQuote = coin.getUsdPriceQuote();
                    value = getItemText(R.string.price_usd, String.format(usdFormat, priceQuote.getPrice()));
                    itemValue.setText(value);
                    break;
                case PRICE_BTC:
                    priceQuote = coin.getBtcPriceQuote();
                    value = getItemText(R.string.price_usd, String.format(usdFormat, priceQuote.getPrice()));
                    itemValue.setText(value);
                    break;
                case VOLUME_24H:
                    priceQuote = coin.getUsdPriceQuote();
                    value = getItemText(R.string.price_usd, String.format(usdFormat, priceQuote.getDayVolume()));
                    itemValue.setText(value);
                    break;
                case MARKET_CAP:
                    priceQuote = coin.getUsdPriceQuote();
                    value = getItemText(R.string.price_usd, String.format(usdFormat, priceQuote.getMarketCap()));
                    itemValue.setText(value);
                    break;
                case AVAILABLE_SUPPLY:
                    value = getItemText(R.string.circulating_supply, String.format(usdFormat, coin.getCirculatingSupply()));
                    itemValue.setText(value);
                    break;
                case TOTAL_SUPPLY:
                    value = getItemText(R.string.total_supply, String.format(usdFormat, coin.getTotalSupply()));
                    itemValue.setText(value);
                    break;
                case CHANGE_1H:
                    priceQuote = coin.getUsdPriceQuote();
                    value = getItemText(R.string.change_1h, String.format(usdFormat, priceQuote.getHourChange()));
                    itemValue.setText(value);
                    break;
                case CHANGE_24H:
                    priceQuote = coin.getUsdPriceQuote();
                    value = getItemText(R.string.change_24h, String.format(usdFormat, priceQuote.getDayChange()));
                    itemValue.setText(value);
                    break;
                case CHANGE_7D:
                    priceQuote = coin.getUsdPriceQuote();
                    value = getItemText(R.string.change_7d, String.format(usdFormat, priceQuote.getWeekChange()));
                    itemValue.setText(value);
                    break;
            }
        }


    }
}
*/
