package com.dreampany.tensor.ui.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreampany.framework.data.model.Base;
import com.dreampany.framework.ui.adapter.SmartAdapter;
import com.dreampany.framework.ui.model.BaseItem;
import com.dreampany.framework.util.TextUtil;
import com.dreampany.tensor.R;
import com.dreampany.tensor.data.enums.MoreType;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by air on 10/18/17.
 */

public class MoreItem extends BaseItem<Base, MoreItem.ViewHolder> implements IFlexible<MoreItem.ViewHolder> {

    private MoreType type;

    public MoreItem(MoreType type) {
        super(null, R.layout.item_more);
        this.type = type;
    }

    public MoreType getType() {
        return type;
    }

    @Override
    public boolean equals(Object inObject) {
        if (MoreItem.class.isInstance(inObject)) {
            MoreItem item = (MoreItem) inObject;
            return type.equals(item.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public boolean filter(Serializable constraint) {
        return false;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        switch (type) {
            case APPS:
                holder.icon.setImageResource(R.drawable.ic_apps_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.more_apps));
                break;
            case RATE_US:
                holder.icon.setImageResource(R.drawable.ic_rate_review_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.rate_us));
                break;
            case ABOUT_US:
                holder.icon.setImageResource(R.drawable.ic_info_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.about_us));
                break;
            case FEEDBACK:
                holder.icon.setImageResource(R.drawable.ic_feedback_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.title_feedback));
                break;
            case SETTINGS:
                holder.icon.setImageResource(R.drawable.ic_settings_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.settings));
                break;
        }
    }

    @Override
    public void unbindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position) {

    }

    @Override
    public void onViewAttached(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position) {

    }

    @Override
    public void onViewDetached(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position) {

    }

    static final class ViewHolder extends SmartAdapter.SmartViewHolder {

        ImageView icon;
        TextView title;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            icon = view.findViewById(R.id.viewIcon);
            title = view.findViewById(R.id.viewTitle);
        }
    }

}
