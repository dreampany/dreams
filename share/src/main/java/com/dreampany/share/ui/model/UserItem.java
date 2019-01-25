package com.dreampany.share.ui.model;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.media.data.model.Media;
import com.dreampany.share.R;
import com.dreampany.share.data.model.User;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Hawladar Roman on 9/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class UserItem extends BaseItem<User, UserItem.ViewHolder> {

    private boolean flagged;

    private UserItem(User item, int layoutId) {
        super(item, layoutId);
    }

    public static UserItem getItem(@NonNull User item) {
        return new UserItem(item, R.layout.item_user);
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return null;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(position, this);
    }

    @Override
    public boolean filter(Serializable constraint) {
        return false;
    }

    static class ViewHolder extends BaseItem.ViewHolder {

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter<IFlexible> adapter) {
            super(view, adapter);
        }

        void bind(int position, UserItem item) {

        }
    }
}
