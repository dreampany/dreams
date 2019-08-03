package com.dreampany.lca.ui.model;

import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.lca.R;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Graph;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;

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
public class GraphItem extends BaseItem<Graph, GraphItem.ViewHolder> {

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

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {

    }

    @Override
    public boolean filter(Serializable constraint) {
        return false;
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

    public ValueFormatter getXAxisValueFormatter() {
        return valueFormatter;
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
    }


}
