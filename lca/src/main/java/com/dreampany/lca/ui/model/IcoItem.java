package com.dreampany.lca.ui.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreampany.frame.data.model.Base;
import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.FrescoUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.model.Ico;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Objects;

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
public class IcoItem extends BaseItem<Ico, IcoItem.ViewHolder, String> {

    private IcoItem(Ico ico, @LayoutRes int layoutId) {
        super(ico, layoutId);
    }

    public static IcoItem getItem(Ico ico) {
        return new IcoItem(ico, R.layout.item_ico);
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        IcoItem item = (IcoItem) inObject;
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
    public boolean filter(String constraint) {
        if (getItem().getName().toLowerCase().startsWith(((String) constraint).toLowerCase())) {
            return true;
        }
        return false;
    }

/*    @Override
    public boolean filter(Serializable constraint) {
        if (item.getName().toLowerCase().startsWith(((String) constraint).toLowerCase())) {
            return true;
        }
        return false;
    }*/

    static class ViewHolder extends BaseItem.ViewHolder {

        SimpleDraweeView icon;
        ImageView timeIcon;
        TextView name;
        TextView description;
        TextView time;

        protected ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);

            icon = view.findViewById(R.id.image_icon);
            timeIcon = view.findViewById(R.id.image_time_icon);
            name = view.findViewById(R.id.text_name);
            description = view.findViewById(R.id.text_description);
            time = view.findViewById(R.id.text_time);
        }

        @DrawableRes
        int getTimeDrawable(IcoStatus status) {
            switch (status) {
                case LIVE:
                case UPCOMING:
                    return R.drawable.ic_clock_start;
                case FINISHED:
                default:
                    return R.drawable.ic_clock_end;
            }
        }

        String getTimeFormat(Ico ico) {
            switch (ico.getStatus()) {
                case LIVE:
                    return TextUtil.getString(getContext(), R.string.ico_live_time, ico.getEndTime());
                case UPCOMING:
                    return TextUtil.getString(getContext(), R.string.ico_upcoming_time, ico.getStartTime());
                case FINISHED:
                default:
                    return TextUtil.getString(getContext(), R.string.ico_finished_time, ico.getEndTime());
            }
        }

        void bind(IcoItem item) {
            Ico ico = item.getItem();
            FrescoUtil.loadImage(icon, ico.getImageUrl(), true);
            name.setText(ico.getName());
            description.setText(ico.getDescription());
            time.setText(getTimeFormat(ico));
            timeIcon.setImageResource(getTimeDrawable(ico.getStatus()));
/*            if (timeDrawable == null) {
                timeDrawable = getContext().getResources().getDrawable(getTimeDrawable(ico.getStatus()));
                time.setCompoundDrawablesWithIntrinsicBounds(timeDrawable, null, null, null);
            }*/
        }

        @Override
        public <VH extends BaseItem.ViewHolder, T extends Base, S extends Serializable, I extends BaseItem<T, VH, S>> void bind(int position, @NotNull I item) {

        }
    }
}
