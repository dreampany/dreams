/*
package com.dreampany.lca.ui.model;

import android.view.View;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import com.dreampany.frame.data.model.Base;
import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.lca.R;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Graph;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

*/
/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

public class GraphItem extends BaseItem<Graph, GraphItem.ViewHolder, String> {

    private Currency currency;
    private LineData lineData;
    private float currentPrice;
    private long currentTime;
    private float differencePrice;
    private float changeInPercent;
    @StringRes
    private int changeInPercentFormat;
    @ColorRes
    private int changeInPercentColor;
    private ValueFormatter valueFormatter;

    private GraphItem(Graph chart,
                      Currency currency,
                      @LayoutRes int layoutId) {
        super(chart, layoutId);
        this.currency = currency;
    }

    public static GraphItem getItem(@NonNull Graph graph, @NonNull Currency currency) {
        return new GraphItem(graph, currency, 0);
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void setDifferencePrice(float differencePrice) {
        this.differencePrice = differencePrice;
    }

    public void setChangeInPercent(float changeInPercent) {
        this.changeInPercent = changeInPercent;
    }

    public void setChangeInPercentFormat(@StringRes int changeInPercentFormat) {
        this.changeInPercentFormat = changeInPercentFormat;
    }

    public void setChangeInPercentColor(@ColorRes int changeInPercentColor) {
        this.changeInPercentColor = changeInPercentColor;
    }

    public void setValueFormatter(ValueFormatter formatter) {
        this.valueFormatter = formatter;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LineData getLineData() {
        return lineData;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public float getDifferencePrice() {
        return differencePrice;
    }

    public float getChangeInPercent() {
        return changeInPercent;
    }

    @StringRes
    public int getChangeInPercentFormat() {
        return changeInPercentFormat;
    }

    @ColorRes
    public int getChangeInPercentColor() {
        return changeInPercentColor;
    }

    public ValueFormatter getValueFormatter() {
        return valueFormatter;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return null;
    }

    @Override
    public boolean filter(String constraint) {
        return false;
    }


    static class ViewHolder extends BaseItem.ViewHolder {

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
        }

        String getText(@StringRes int keyResId) {
            return getContext().getString(keyResId);
        }

        String getItemText(@StringRes int keyResId, String value) {
            return String.format(
                    getText(R.string.coin_format),
                    getText(keyResId),
                    value);
        }

        String getItemText(@StringRes int keyResId, double value) {
            return String.format(
                    getText(R.string.coin_format),
                    getText(keyResId),
                    value);
        }

        @Override
        public <VH extends BaseItem.ViewHolder, T extends Base, S extends Serializable, I extends BaseItem<T, VH, S>> void bind(int position, @NotNull I item) {

        }
    }


}
*/
