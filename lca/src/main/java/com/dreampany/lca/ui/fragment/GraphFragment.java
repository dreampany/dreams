package com.dreampany.lca.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.FragmentScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.widget.SmartNestedScrollView;
import com.dreampany.frame.ui.widget.toggle.SingleSelectToggleGroup;
import com.dreampany.frame.util.ColorUtil;
import com.dreampany.frame.util.DisplayUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.databinding.FragmentGraphBinding;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.enums.TimeType;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.GraphItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.GraphViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;



/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@FragmentScope
public class GraphFragment
        extends BaseFragment
        implements BootstrapDropDown.OnDropDownItemClickListener,
        SingleSelectToggleGroup.OnCheckedChangeListener,
        OnChartValueSelectedListener {

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentGraphBinding binding;
    private GraphViewModel vm;
    private SmartNestedScrollView scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private BootstrapDropDown currencyDropDown;

    private LineChart chart;
    private int displayWidth;
    private TimeType timeType;

    @Inject
    public GraphFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_graph;
    }

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Screen.coinGraph(getAppContext());
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        vm.start();
        vm.load(timeType, false);
    }

    @Override
    protected void onStopUi() {
        processUiState(UiState.HIDE_PROGRESS);
        vm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        processUiState(UiState.HIDE_PROGRESS);
        super.onPause();
        //vm.removeSingleSubscription();
    }

    @Override
    public void onRefresh() {
        vm.load(timeType, true);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.button_more:
                openMoreGraphSite();
                break;
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View v, int id) {
        String currencyValue = currencyDropDown.getDropdownData()[id];
        Currency currency = Currency.valueOf(currencyValue);
        if (currency != vm.getCurrentCurrency()) {
            vm.setCurrentCurrencyCode(currencyValue);
            binding.dropDownCurrency.setText(currencyValue);
            vm.load(timeType, true);
        }
    }

    @Override
    public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
        TimeType timeType;
        switch (checkedId) {
            case R.id.toggle_day:
                timeType = TimeType.DAY;
                break;
            case R.id.toggle_week:
                timeType = TimeType.WEEK;
                break;
            case R.id.toggle_month:
                timeType = TimeType.MONTH;
                break;
            case R.id.toggle_three_month:
                timeType = TimeType.THREE_MONTH;
                break;
            case R.id.toggle_six_month:
                timeType = TimeType.SIX_MONTH;
                break;
            case R.id.toggle_year:
                timeType = TimeType.YEAR;
                break;
            case R.id.toggle_all:
            default:
                timeType = TimeType.ALL;
                break;
        }
        if (this.timeType != timeType) {
            this.timeType = timeType;
            vm.load(timeType, true);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        updatePrice(e.getY());
        updateDate((long) e.getX());
    }

    @Override
    public void onNothingSelected() {

    }

    private void initView() {
        binding = (FragmentGraphBinding) super.binding;
        refresh = binding.layoutRefresh;
        expandable = binding.layoutTop.layoutExpandable;

        currencyDropDown = binding.dropDownCurrency;
        ViewUtil.setSwipe(refresh, this);

        vm = ViewModelProviders.of(this, factory).get(GraphViewModel.class);
        UiTask<Coin> uiTask = getCurrentTask(true);
        vm.setTask(uiTask);
        vm.observeUiState(this, this::processUiState);
        //vm.observeEvent(this, this::processEvent);
        vm.observeOutput(this, this::processResponse);

        displayWidth = DisplayUtil.getScreenWidthInPx(Objects.requireNonNull(getContext()));
        scroller = binding.smartScroller;
        chart = binding.lineChart;
        timeType = TimeType.DAY;

        binding.buttonMore.setOnClickListener(this);
        binding.buttonMore.setText(TextUtil.toUnderscore(getContext(), R.string.more_graph));

        currencyDropDown.setOnDropDownItemClickListener(this);
        binding.layoutToggleDate.toggleDate.setOnCheckedChangeListener(this);
        currencyDropDown.setText(vm.getCurrentCurrency().name());
        initLineChart();
    }

    private void initLineChart() {
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chart.getXAxis().setAvoidFirstLastClipping(true);
        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setContentDescription("");
        chart.setNoDataText(getString(R.string.empty));
        chart.setNoDataTextColor(R.color.darkRed);
        chart.setOnChartValueSelectedListener(this);
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                YAxis yAxis = chart.getAxisLeft();
                if (me.getX() > yAxis.getLongestLabel().length() * yAxis.getTextSize() &&
                        me.getX() < displayWidth - chart.getViewPortHandler().offsetRight()) {
                    scroller.setScrollable(false);
                }
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                scroller.setScrollable(true);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                if (!refresh.isRefreshing()) {
                    refresh.setRefreshing(true);
                }
                break;
            case HIDE_PROGRESS:
                if (refresh.isRefreshing()) {
                    refresh.setRefreshing(false);
                }
                break;
            case OFFLINE:
                expandable.expand();
                break;
            case ONLINE:
                expandable.collapse();
                break;
            case EMPTY:
            case ERROR:
                break;
        }
    }


    private void processResponse(Response<GraphItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress<GraphItem> progress = (Response.Progress<GraphItem>) response;
            processProgress(progress.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure failure = (Response.Failure) response;
            processFailure(failure.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<GraphItem> result = (Response.Result<GraphItem>) response;
            processResult(Objects.requireNonNull(result.getData()));
        }
    }

    private void processProgress(boolean loading) {
        if (loading) {
            vm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
            vm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }

    private void processFailure(Throwable error) {
        if (error instanceof IOException || error.getCause() instanceof IOException) {
            vm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            vm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
            vm.updateUiState(UiState.EXTRA);
        } else if (error instanceof MultiException) {
            for (Throwable e : ((MultiException) error).getErrors()) {
                processFailure(e);
            }
        }
    }


    private void processResult(GraphItem item) {
        if (!item.isSuccess()) {
            vm.updateUiState(UiState.EMPTY);
            return;
        }
        updatePrice(item.getCurrentPrice());
        updateDate(item.getCurrentTime());
        updateChange(item.getDifferencePrice(), item.getChangeInPercent(), item.getChangeInPercentFormat(), item.getChangeInPercentColor());
        binding.lineChart.getXAxis().setValueFormatter(item.getXAxisValueFormatter());
        binding.lineChart.setData(item.getLineData());
        binding.lineChart.animateX(800);
    }

    private void updatePrice(float price) {
        String priceData = vm.getFormattedPrice(Currency.USD, price);
        binding.textPrice.setText(priceData);
    }

    private void updateDate(long time) {
        binding.textTime.setText(TimeUtil.getFullTime(time));
    }

    private void updateChange(float differencePrice, float changeInPercent, int format, int color) {
        String timeTypeValue = vm.getTimeTypeValue(timeType);
        String change = String.format(TextUtil.getString(getContext(), format), timeTypeValue, changeInPercent, Math.abs(differencePrice));
        binding.textChange.setText(change);
        binding.textChange.setTextColor(ColorUtil.getColor(getContext(), color));
    }

    public void openMoreGraphSite() {

        UiTask<Coin> task = getCurrentTask();
        Coin coin = task.getInput();
        String webUrl = Constants.Api.CoinMarketCapSiteUrl;
        String url = String.format(webUrl, coin.getSlug());

        UiTask<Graph> outTask = new UiTask<>(true);
        outTask.setComment(url);
        outTask.setUiType(UiType.GRAPH);
        outTask.setSubtype(UiSubtype.VIEW);
        openActivity(ToolsActivity.class, outTask);
    }
}
