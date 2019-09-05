package com.dreampany.play2048.ui.model;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.dreampany.play2048.data.model.Demo;
import com.dreampany.framework.ui.model.BaseItem;

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
public class DemoItem extends BaseItem<Demo, DemoItem.ViewHolder> {

    private DemoItem(Demo item, @LayoutRes int layoutId) {
        super(item, layoutId);
    }

    public static DemoItem getSimpleItem(Demo item, @LayoutRes int layoutId) {
        return new DemoItem(item, layoutId);
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

        protected ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
        }

        void bind(DemoItem item) {
        }
    }
}
