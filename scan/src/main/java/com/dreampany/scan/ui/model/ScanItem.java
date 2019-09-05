package com.dreampany.scan.ui.model;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.dreampany.scan.R;
import com.dreampany.scan.data.model.Scan;
import com.dreampany.framework.ui.adapter.SmartAdapter;
import com.dreampany.framework.ui.model.BaseItem;
import com.dreampany.scan.ui.adapter.ScanAdapter;
import com.google.common.base.Objects;
import com.like.LikeButton;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ScanItem extends BaseItem<Scan, ScanItem.ViewHolder> {

    private boolean flagged;

    private ScanItem(Scan scan, @LayoutRes int layoutId) {
        super(scan, layoutId);
    }

    public static ScanItem getItem(Scan scan) {
        return new ScanItem(scan, R.layout.item_scan);
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isFlagged() {
        return flagged;
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        ScanItem item = (ScanItem) inObject;
        return Objects.equal(item.getItem(), getItem());
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
        if (item.getText().toLowerCase().startsWith(((String) constraint).toLowerCase())) {
            return true;
        }
        return false;
    }

    static class ViewHolder extends SmartAdapter.SmartViewHolder {

        ScanAdapter adapter;
        TextView text;
        BootstrapLabel label;
        LikeButton like;

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            this.adapter = (ScanAdapter) adapter;
            text = view.findViewById(R.id.text_scan);
            label = view.findViewById(R.id.label_scan);
            like = view.findViewById(R.id.button_like);

            like.setOnClickListener(this.adapter.getClickListener());
        }

        void bind(ScanItem item) {
            Scan scan = item.getItem();
            text.setText(scan.getText());
            label.setText(scan.getType().name());
            like.setTag(scan);
            like.setLiked(item.isFlagged());
        }
    }
}
