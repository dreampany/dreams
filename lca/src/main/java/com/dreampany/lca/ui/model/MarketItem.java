package com.dreampany.lca.ui.model;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.ColorUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Market;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MarketItem extends BaseItem<Market, MarketItem.ViewHolder> {

    private String volume24h;
    private String price;
    @StringRes
    private int changePct24hFormat;
    @ColorRes
    private int changePct24hColor;

    private MarketItem(@NonNull Market market,
                       @LayoutRes int layoutId) {
        super(market, layoutId);
    }

    public static MarketItem getItem(@NonNull Market exchange) {
        return new MarketItem(exchange, R.layout.item_market);
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(this);
    }

    @Override
    public boolean filter(Serializable constraint) {
        return false;
    }

    public void setVolume24h(String volume24h) {
        this.volume24h = volume24h;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setChangePct24hFormat(int changePct24hFormat) {
        this.changePct24hFormat = changePct24hFormat;
    }

    public void setChangePct24hColor(int changePct24hColor) {
        this.changePct24hColor = changePct24hColor;
    }

    static class ViewHolder extends BaseItem.ViewHolder {

        TextView name;
        TextView volume24h;
        TextView changePct24h;
        TextView price;

        protected ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            name = view.findViewById(R.id.text_name);
            volume24h = view.findViewById(R.id.text_volume24h);
            changePct24h = view.findViewById(R.id.text_change_pct24h);
            price = view.findViewById(R.id.text_price);
        }

        void bind(MarketItem item) {
            Market market = item.getItem();
            name.setText(market.getMarket());
            volume24h.setText(item.volume24h);
            changePct24h.setText(TextUtil.getString(getContext(), item.changePct24hFormat, market.getChangePct24h()));
            changePct24h.setTextColor(ColorUtil.getColor(getContext(), item.changePct24hColor));
            price.setText(item.price);
        }

        String getText(@StringRes int keyResId) {
            return getContext().getString(keyResId);
        }

        String getItemText(@StringRes int keyResId, String value) {
            return String.format(
                    getText(R.string.coin_format),
                    getText(keyResId),
                    value);
        }

        String getItemText(@StringRes int keyResId, double value) {
            return String.format(
                    getText(R.string.coin_format),
                    getText(keyResId),
                    value);
        }
    }


}
