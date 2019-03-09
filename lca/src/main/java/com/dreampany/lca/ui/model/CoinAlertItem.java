package com.dreampany.lca.ui.model;

import android.view.View;
import androidx.annotation.LayoutRes;
import butterknife.ButterKnife;
import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.google.common.base.Objects;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Roman-372 on 3/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class CoinAlertItem extends BaseItem<CoinAlert, CoinAlertItem.ViewHolder> {

    private Coin coin;
    private boolean empty;
    private boolean saveOperation;

    private CoinAlertItem(Coin coin, CoinAlert alert, @LayoutRes int layoutId) {
        super(alert, layoutId);
        this.coin = coin;
    }

    public static CoinAlertItem getItem(Coin coin, CoinAlert alert) {
        return new CoinAlertItem(coin, alert, 0);
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
        return false;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setSaveOperation(boolean saveOperation) {
        this.saveOperation = saveOperation;
    }

    public Coin getCoin() {
        return coin;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isSaveOperation() {
        return saveOperation;
    }

    static abstract class ViewHolder extends BaseItem.ViewHolder {

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        abstract void bind(int position, CoinAlertItem item);
    }

    static final class ItemViewHolder extends CoinAlertItem.ViewHolder {

        ItemViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
        }

        @Override
        void bind(int position, CoinAlertItem item) {

        }
    }
}
