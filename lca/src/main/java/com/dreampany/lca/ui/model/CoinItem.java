package com.dreampany.lca.ui.model;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.ColorUtil;
import com.dreampany.frame.util.FrescoUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.misc.CurrencyFormatter;
import com.dreampany.lca.ui.adapter.CoinAdapter;
import com.dreampany.lca.ui.enums.CoinItemType;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Objects;
import com.like.LikeButton;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinItem extends BaseItem<Coin, CoinItem.ViewHolder> {

    private CoinItemType type;
    private Currency currency;
    private boolean favorite;

/*    private CoinItem(Coin coin, CoinItemType type, @LayoutRes int layoutId) {
        this(coin, Currency.USD, type, layoutId);
    }*/

    private CoinItem(Coin coin, Currency currency, CoinItemType type, @LayoutRes int layoutId) {
        super(coin, layoutId);
        this.currency = currency;
        this.type = type;
    }

    public static CoinItem getProgressItem() {
        return new CoinItem(null, null, CoinItemType.PROGRESS, R.layout.item_progress);
    }

    public static CoinItem getSimpleItem(@NonNull Coin coin, Currency currency) {
        return new CoinItem(coin, currency, CoinItemType.SIMPLE, R.layout.item_coin);
    }

    public static CoinItem getDetailsItem(@NonNull Coin coin, Currency currency) {
        return new CoinItem(coin, currency, CoinItemType.DETAILS, R.layout.item_coin_details);
    }

    public static CoinItem getQuoteItem(@NonNull Coin coin, @NonNull Currency currency) {
        return new CoinItem(coin, currency, CoinItemType.QUOTE, R.layout.item_coin_quote);
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;
        CoinItem item = (CoinItem) in;
        return Objects.equal(item.getItem(), getItem())
                && item.type == type
                && (item.type != CoinItemType.QUOTE || item.currency == currency);
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        switch (type) {
            case PROGRESS:
                return new ProgressViewHolder(view, adapter);
            case SIMPLE:
                return new SimpleViewHolder(view, adapter);
            case DETAILS:
                return new DetailsViewHolder(view, adapter);
            case QUOTE:
            default:
                return new QuoteViewHolder(view, adapter);
        }
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(position, this);
    }

    @Override
    public boolean filter(Serializable constraint) {
        String keyword = item.getName() + item.getSymbol() + item.getSlug();
        return keyword.toLowerCase().contains(((String) constraint).toLowerCase());
    }

    static abstract class ViewHolder extends BaseItem.ViewHolder {

        final CoinAdapter adapter;
        final String usdFormat;
        final String btcFormat;
        final int positiveChange;
        final int negativeChange;
        final CurrencyFormatter formatter;

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            this.adapter = (CoinAdapter) adapter;
            usdFormat = getText(R.string.usd_format);
            btcFormat = getText(R.string.btc_format);
            positiveChange = R.string.positive_pct_format;
            negativeChange = R.string.negative_pct_format;
            formatter = new CurrencyFormatter(getContext());
        }

        abstract void bind(int position, CoinItem item);

        String getText(@StringRes int resId) {
            return getContext().getString(resId);
        }

        String getItemText(@StringRes int formatRes, Object... values) {
            return String.format(getText(formatRes), values);
        }
    }

    static final class ProgressViewHolder extends ViewHolder {

        ProgressViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
            super(view, adapter);
        }

        @Override
        void bind(int position, CoinItem item) {

        }
    }

    static final class SimpleViewHolder extends ViewHolder {

        @BindView(R.id.image_icon)
        SimpleDraweeView icon;
        @BindView(R.id.text_name)
        TextView name;
        @BindView(R.id.text_price)
        TextView price;
        @BindView(R.id.text_change_1h)
        TextView hourChange;
        @BindView(R.id.text_change_24h)
        TextView dayChange;
        @BindView(R.id.text_change_7d)
        TextView weekChange;
        @BindView(R.id.text_market_cap)
        TextView marketCap;
        @BindView(R.id.text_volume_24h)
        TextView dayVolume;
/*        @BindView(R.id.button_like)
        LikeButton like;*/

        SimpleViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
            super(view, adapter);
            //like.setOnClickListener(super.adapter.getClickListener());
        }

        @Override
        void bind(int position, CoinItem item) {
            Coin coin = item.getItem();
            String imageUrl = String.format(Locale.ENGLISH, Constants.ImageUrl.CoinMarketCapImageUrl, coin.getCoinId());
            FrescoUtil.loadImage(icon, imageUrl, true);
            String nameText = String.format(Locale.ENGLISH, getText(R.string.full_name), coin.getSymbol(), coin.getName());
            name.setText(nameText);

            Quote quote = coin.getUsdQuote();

            double price = 0f;
            double hourChange = 0f;
            double dayChange = 0f;
            double weekChange = 0f;
            double marketCap = 0f;
            double dayVolume = 0f;
            if (quote != null) {
                price = quote.getPrice();
                hourChange = quote.getHourChange();
                dayChange = quote.getDayChange();
                weekChange = quote.getWeekChange();
                marketCap = quote.getMarketCap();
                dayVolume = quote.getDayVolume();
            }

            this.price.setText(String.format(usdFormat, price));
            this.marketCap.setText(formatter.formatPriceAsDollar(marketCap));
            this.dayVolume.setText(formatter.formatPriceAsDollar(dayVolume));

            int hourFormat = hourChange >= 0.0f ? positiveChange : negativeChange;
            int dayFormat = dayChange >= 0.0f ? positiveChange : negativeChange;
            int weekFormat = weekChange >= 0.0f ? positiveChange : negativeChange;

            this.hourChange.setText(String.format(getText(hourFormat), hourChange));
            this.dayChange.setText(String.format(getText(dayFormat), dayChange));
            this.weekChange.setText(String.format(getText(weekFormat), weekChange));

            int startColor = R.color.material_grey700;
            int endColor = dayChange >= 0 ? R.color.material_green700 : R.color.material_red700;
            ViewUtil.blink(this.price, startColor, endColor);

            int hourChangeColor = hourChange >= 0 ? R.color.material_green700 : R.color.material_red700;
            this.hourChange.setTextColor(ColorUtil.getColor(getContext(), hourChangeColor));

            int dayChangeColor = dayChange >= 0 ? R.color.material_green700 : R.color.material_red700;
            this.dayChange.setTextColor(ColorUtil.getColor(getContext(), dayChangeColor));

            int weekChangeColor = weekChange >= 0 ? R.color.material_green700 : R.color.material_red700;
            this.weekChange.setTextColor(ColorUtil.getColor(getContext(), weekChangeColor));
        }
    }


    static final class DetailsViewHolder extends ViewHolder {

        @BindView(R.id.image_icon)
        SimpleDraweeView icon;
        @BindView(R.id.text_name)
        TextView name;
        @BindView(R.id.text_price)
        TextView price;
        @BindView(R.id.button_like)
        LikeButton like;

        @BindView(R.id.layout_market_cap)
        View marketCap;
        @BindView(R.id.layout_volume)
        View volume;

        TextView marketCapTitle;
        TextView marketCapValue;
        TextView volumeTitle;
        TextView volumeValue;

        @BindView(R.id.text_change_1h)
        TextView hourChange;
        @BindView(R.id.text_change_24h)
        TextView dayChange;
        @BindView(R.id.text_change_7d)
        TextView weekChange;


        DetailsViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
            super(view, adapter);
            marketCapTitle = marketCap.findViewById(R.id.text_title);
            marketCapValue = marketCap.findViewById(R.id.text_value);
            volumeTitle = volume.findViewById(R.id.text_title);
            volumeValue = volume.findViewById(R.id.text_value);

            like.setOnClickListener(super.adapter.getClickListener());
        }

        @Override
        void bind(int position, CoinItem item) {
            Coin coin = item.getItem();

            String imageUrl = String.format(Locale.ENGLISH, Constants.ImageUrl.CoinMarketCapImageUrl, coin.getCoinId());
            FrescoUtil.loadImage(icon, imageUrl, true);
            String nameText = String.format(Locale.ENGLISH, getText(R.string.full_name), coin.getSymbol(), coin.getName());
            name.setText(nameText);

            Quote quote = coin.getQuote(item.currency);
            if (quote != null) {
                double price = quote.getPrice();
                double hourChange = quote.getHourChange();
                double dayChange = quote.getDayChange();
                double weekChange = quote.getWeekChange();

                int hourFormat = hourChange >= 0.0f ? positiveChange : negativeChange;
                int dayFormat = dayChange >= 0.0f ? positiveChange : negativeChange;
                int weekFormat = weekChange >= 0.0f ? positiveChange : negativeChange;


                this.price.setText(String.format(usdFormat, price));

                marketCapTitle.setText(R.string.market_cap);
                volumeTitle.setText(R.string.volume_24h);

                String oneHourValue = String.format(getText(hourFormat), hourChange);
                String oneDayValue = String.format(getText(dayFormat), dayChange);
                String weekValue = String.format(getText(weekFormat), weekChange);

                marketCapValue.setText(formatter.formatPriceAsDollar(quote.getMarketCap()));
                volumeValue.setText(formatter.formatPriceAsDollar(quote.getDayVolume()));

                this.hourChange.setText(getItemText(R.string.coin_format, getText(R.string.one_hour), oneHourValue));
                this.dayChange.setText(getItemText(R.string.coin_format, getText(R.string.one_day), oneDayValue));
                this.weekChange.setText(getItemText(R.string.coin_format, getText(R.string.week), weekValue));

                int change1hColor = hourChange >= 0.0f ? R.color.material_green700 : R.color.material_red700;
                int change24hColor = dayChange >= 0.0f ? R.color.material_green700 : R.color.material_red700;
                int change7dColor = weekChange >= 0.0f ? R.color.material_green700 : R.color.material_red700;

                this.hourChange.setTextColor(ColorUtil.getColor(getContext(), change1hColor));
                this.dayChange.setTextColor(ColorUtil.getColor(getContext(), change24hColor));
                this.weekChange.setTextColor(ColorUtil.getColor(getContext(), change7dColor));
            }

            like.setTag(coin);
            like.setLiked(item.isFavorite());
        }
    }

    static final class QuoteViewHolder extends ViewHolder {

        @BindView(R.id.layout_circulating)
        View circulatingSupply;
        @BindView(R.id.layout_total)
        View totalSupply;
        @BindView(R.id.layout_max)
        View maxSupply;

        TextView circulatingTitle;
        TextView circulatingValue;
        TextView totalTitle;
        TextView totalValue;
        TextView maxTitle;
        TextView maxValue;

        @BindView(R.id.text_last_updated)
        TextView lastUpdated;

        QuoteViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
            super(view, adapter);

            circulatingTitle = circulatingSupply.findViewById(R.id.text_title);
            circulatingValue = circulatingSupply.findViewById(R.id.text_value);

            totalTitle = totalSupply.findViewById(R.id.text_title);
            totalValue = totalSupply.findViewById(R.id.text_value);

            maxTitle = maxSupply.findViewById(R.id.text_title);
            maxValue = maxSupply.findViewById(R.id.text_value);
        }

        @Override
        void bind(int position, CoinItem item) {
            Coin coin = item.getItem();
            String symbol = coin.getSymbol();

            String circulating = formatter.formatPrice(coin.getCirculatingSupply()).concat(" ").concat(symbol);
            String total = formatter.formatPrice(coin.getTotalSupply()).concat(" ").concat(symbol);
            String max = formatter.formatPrice(coin.getMaxSupply()).concat(" ").concat(symbol);

            circulatingTitle.setText(R.string.circulating_supply);
            totalTitle.setText(R.string.total_supply);
            maxTitle.setText(R.string.max_supply);

            circulatingValue.setText(circulating);
            totalValue.setText(total);
            maxValue.setText(max);
            String lastUpdatedTime = (String) DateUtils.getRelativeTimeSpanString(
                    coin.getLastUpdated(),
                    TimeUtil.currentTime(),
                    DateUtils.MINUTE_IN_MILLIS);
            lastUpdated.setText(lastUpdatedTime);
        }
    }
}
