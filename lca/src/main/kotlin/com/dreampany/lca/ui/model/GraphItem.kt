package com.dreampany.lca.ui.model

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.ui.view.TextViewClickMovement
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.model.Graph
import com.dreampany.lca.misc.Constants
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class GraphItem
private constructor(
    item: Graph,
    currency: Currency,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Graph, GraphItem.ViewHolder, String>(item, layoutId) {

    private var lineData: LineData? = null
    private var currentPrice: Float = 0.toFloat()
    private var currentTime: Long = 0
    private var differencePrice: Float = 0.toFloat()
    private var changeInPercent: Float = 0.toFloat()
    @StringRes
    private var changeInPercentFormat: Int = 0
    @ColorRes
    private var changeInPercentColor: Int = 0
    private var valueFormatter: ValueFormatter? = null

    companion object {
        fun getItem(graph: Graph, currency: Currency): GraphItem {
            return GraphItem(graph, currency, 0)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String?): Boolean {
        return false
    }

    fun setLineData(lineData: LineData) {
        this.lineData = lineData
    }

    fun setCurrentPrice(currentPrice: Float) {
        this.currentPrice = currentPrice
    }

    fun setCurrentTime(currentTime: Long) {
        this.currentTime = currentTime
    }

    fun setDifferencePrice(differencePrice: Float) {
        this.differencePrice = differencePrice
    }

    fun setChangeInPercent(changeInPercent: Float) {
        this.changeInPercent = changeInPercent
    }

    fun setChangeInPercentFormat(@StringRes changeInPercentFormat: Int) {
        this.changeInPercentFormat = changeInPercentFormat
    }

    fun setChangeInPercentColor(@ColorRes changeInPercentColor: Int) {
        this.changeInPercentColor = changeInPercentColor
    }

    fun setValueFormatter(formatter: ValueFormatter) {
        this.valueFormatter = formatter
    }

    fun getLineData(): LineData? {
        return lineData
    }

    fun getCurrentPrice(): Float {
        return currentPrice
    }

    fun getCurrentTime(): Long {
        return currentTime
    }

    fun getDifferencePrice(): Float {
        return differencePrice
    }

    fun getChangeInPercent(): Float {
        return changeInPercent
    }

    @StringRes
    fun getChangeInPercentFormat(): Int {
        return changeInPercentFormat
    }

    @ColorRes
    fun getChangeInPercentColor(): Int {
        return changeInPercentColor
    }

    fun getValueFormatter(): ValueFormatter? {
        return valueFormatter
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {
        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {

        }

    }
}