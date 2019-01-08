package com.dreampany.lca.ui.model;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.More;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by air on 10/18/17.
 */

public class MoreItem extends BaseItem<More, MoreItem.ViewHolder> implements IFlexible<MoreItem.ViewHolder> {

    private MoreItem(@NonNull More item, @LayoutRes int layoutId) {
        super(item, layoutId);
    }

    public static MoreItem getItem(@NonNull More item) {
        return new MoreItem(item, R.layout.item_more);
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
        switch (item.getType()) {
            case APPS:
                holder.icon.setImageResource(R.drawable.ic_apps_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.more_apps));
                break;
            case RATE_US:
                holder.icon.setImageResource(R.drawable.ic_rate_review_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.rate_us));
                break;
            case FEEDBACK:
                holder.icon.setImageResource(R.drawable.ic_feedback_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.title_feedback));
                break;
            case SETTINGS:
                holder.icon.setImageResource(R.drawable.ic_settings_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.settings));
                break;
            case LICENSE:
                holder.icon.setImageResource(R.drawable.ic_security_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.license));
                break;
            case ABOUT:
                holder.icon.setImageResource(R.drawable.ic_info_black_24dp);
                holder.title.setText(TextUtil.getString(holder.getContext(), R.string.about));
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

    static final class ViewHolder extends BaseItem.ViewHolder {

        ImageView icon;
        TextView title;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            icon = view.findViewById(R.id.viewIcon);
            title = view.findViewById(R.id.viewTitle);
        }
    }

}
