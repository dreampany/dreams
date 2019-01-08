package com.dreampany.lca.ui.model;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Exchange;

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
public class ExchangeItem extends BaseItem<Exchange, ExchangeItem.ViewHolder> {

    private ExchangeItem(@NonNull Exchange exchange,
                         @LayoutRes int layoutId) {
        super(exchange, layoutId);
    }

    public static ExchangeItem getItem(@NonNull Exchange exchange) {
        return new ExchangeItem(exchange, 0);
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {

    }

    @Override
    public boolean filter(Serializable constraint) {
        return false;
    }

    static class ViewHolder extends BaseItem.ViewHolder {

        protected ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
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
