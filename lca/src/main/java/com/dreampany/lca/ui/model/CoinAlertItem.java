package com.dreampany.lca.ui.model;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.FrescoUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.adapter.CoinAlertAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Objects;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Roman-372 on 3/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class CoinAlertItem extends BaseItem<CoinAlert, CoinAlertItem.ViewHolder> {

    private Coin coin;
    private boolean empty;
    private boolean deleting;

    private CoinAlertItem(Coin coin, CoinAlert alert, @LayoutRes int layoutId) {
        super(alert, layoutId);
        this.coin = coin;
    }

    public static CoinAlertItem getItem(Coin coin, CoinAlert alert) {
        return new CoinAlertItem(coin, alert, R.layout.item_coin_alert);
    }

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;
        CoinAlertItem item = (CoinAlertItem) in;
        return Objects.equal(item.getItem(), getItem());
    }

    @Override
    public CoinAlertItem.ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, CoinAlertItem.ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(position, this);
    }

    @Override
    public boolean filter(Serializable constraint) {
        String keyword = coin.getName() + coin.getSymbol() + coin.getSlug();
        return keyword.toLowerCase().contains(((String) constraint).toLowerCase());
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setDeleting(boolean deleting) {
        this.deleting = deleting;
    }

    public Coin getCoin() {
        return coin;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean getDeleting() {
        return deleting;
    }

    static abstract class ViewHolder extends BaseItem.ViewHolder {

        final CoinAlertAdapter adapter;
        final String usdFormat;

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            this.adapter = (CoinAlertAdapter) adapter;
            usdFormat = getText(R.string.usd_format);
        }

        abstract void bind(int position, CoinAlertItem item);

        String getText(@StringRes int resId) {
            return getContext().getString(resId);
        }
    }

    static final class ItemViewHolder extends CoinAlertItem.ViewHolder {

/*        @BindView(R.id.layout_left_view)
        View leftView;
        @BindView(R.id.layout_right_view)
        View rightView;
        @BindView(R.id.layout_front_view)
        View frontView;*/

        @BindView(R.id.image_icon)
        SimpleDraweeView icon;
        @BindView(R.id.text_name)
        TextView name;
        @BindView(R.id.text_price)
        TextView price;

        @BindView(R.id.layout_price_up)
        View layoutPriceUp;
        @BindView(R.id.layout_price_down)
        View layoutPriceDown;

        @BindView(R.id.text_price_up)
        TextView textPriceUp;
        @BindView(R.id.text_price_down)
        TextView textPriceDown;

        @BindView(R.id.image_delete)
        View imageDelete;

        ItemViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            imageDelete.setOnClickListener(super.adapter.getClickListener());
        }

/*        @Override
        public View getFrontView() {
            return frontView;
        }

        @Override
        public View getRearLeftView() {
            return leftView;
        }

        @Override
        public View getRearRightView() {
            return rightView;
        }*/

        @Override
        void bind(int position, CoinAlertItem item) {
            Coin coin = item.getCoin();
            String imageUrl = String.format(Locale.ENGLISH, Constants.ImageUrl.CoinMarketCapImageUrl, coin.getId());
            FrescoUtil.loadImage(icon, imageUrl, true);
            String nameText = String.format(Locale.ENGLISH, getText(R.string.full_name), coin.getSymbol(), coin.getName());
            name.setText(nameText);

            Quote quote = coin.getQuote(Currency.USD);
            double price = 0f;
            if (quote != null) {
                price = quote.getPrice();
            }
            this.price.setText(String.format(usdFormat, price));

            CoinAlert alert = item.getItem();
            if (alert.hasPriceUp()) {
                textPriceUp.setText(String.format(usdFormat, alert.getPriceUp()));
            } else {
                ViewUtil.hide(layoutPriceUp);
            }
            if (alert.hasPriceDown()) {
                textPriceDown.setText(String.format(usdFormat, alert.getPriceDown()));
            } else {
                ViewUtil.hide(layoutPriceDown);
            }

            if (item.getDeleting()) {
                ViewUtil.visible(imageDelete);
            } else {
                ViewUtil.hide(imageDelete);
            }
            imageDelete.setTag(item);
        }
    }
}
