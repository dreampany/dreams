package com.dreampany.share.ui.model;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.View;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.network.data.model.Network;
import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NetworkItem extends BaseItem<Network, NetworkItem.ViewHolder> {


    private NetworkItem(@NonNull Network item, @LayoutRes int layoutId) {
        super(item, layoutId);
    }

    public static NetworkItem getItem(@NonNull Network item) {
        return new NetworkItem(item, 0);
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        NetworkItem item = (NetworkItem) inObject;
        return Objects.equal(item.getItem(), getItem());
    }

    @Override
    public boolean filter(Serializable constraint) {
        return item.getSsid().toLowerCase().startsWith(((String) constraint).toLowerCase());
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ViewHolder(view, adapter);
    }


    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(position, this);
    }

    static class ViewHolder extends BaseItem.ViewHolder {

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        void bind(int position, NetworkItem item) {

        }
    }
}
