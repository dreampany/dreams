package com.dreampany.word.ui.model;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.word.data.model.Load;

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
public class LoadItem extends BaseItem<Load, LoadItem.ViewHolder> {

    private LoadItem(Load item, @LayoutRes int layoutId) {
        super(item, layoutId);
    }

    public static LoadItem getSimpleItem() {
        return new LoadItem(null, 0);
    }

    public static LoadItem getSimpleItem(Load item, @LayoutRes int layoutId) {
        return new LoadItem(item, layoutId);
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

    static class ViewHolder extends BaseItem.ViewHolder {

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
        }

        void bind(LoadItem item) {
        }
    }
}
