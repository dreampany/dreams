package com.dreampany.frame.ui.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.common.base.Objects;

import java.io.Serializable;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by Hawladar Roman on 30/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 */
public abstract class BaseItem<T, VH extends BaseItem.ViewHolder> extends AbstractFlexibleItem<VH> implements IFilterable, Serializable {

    protected T item;
    @LayoutRes
    protected int layoutId;
    protected boolean success;

    protected BaseItem(T item, int layoutId) {
        this.item = item;
        this.layoutId = layoutId;
        this.success = true;
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        BaseItem item = (BaseItem) inObject;
        return Objects.equal(item, item.item);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(item);
    }

    public void setItem(T item) {
        this.item = item;
    }

    public void setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @LayoutRes
    @Override
    public int getLayoutRes() {
        return layoutId;
    }

    @NonNull
    public T getItem() {
        return item;
    }

    public boolean isSuccess() {
        return success;
    }


    public static abstract class ViewHolder extends FlexibleViewHolder {

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }
}
