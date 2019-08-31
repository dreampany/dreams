package com.dreampany.lca.vm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.annimon.stream.Stream;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.util.*;
import com.dreampany.frame.ui.vm.BaseViewModel;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.data.model.News;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.data.source.repository.GraphRepository;
import com.dreampany.lca.misc.*;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.activity.WebActivity;
import com.dreampany.lca.ui.enums.TimeType;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.GraphItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.manager.NetworkManager;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import im.delight.android.webview.AdvancedWebView;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class GraphViewModel
        extends BaseViewModel<Graph, GraphItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private final NetworkManager network;
    private final Pref pref;
    private final GraphRepository repo;
    private CurrencyFormatter formatter;
    private TimeType timeType;
    private GraphItem lastResult;

    @Inject
    GraphViewModel(Application application,
                   RxMapper rx,
                   AppExecutors ex,
                   ResponseMapper rm,
                   NetworkManager network,
                   Pref pref,
                   CurrencyFormatter formatter,
                   GraphRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
        this.formatter = formatter;
    }

    @Override
    public void clear() {
        network.deObserve(this);
        super.clear();
    }

    @Override
    public void onNetworkResult(@NonNull List<Network> networks) {

        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.getInternet()) {
                state = UiState.ONLINE;
                Response<GraphItem> result = getOutput().getValue();
                if (result == null || result instanceof Response.Failure) {
                    boolean empty = lastResult == null || !lastResult.isSuccess();
                    getEx().postToUi(() -> load(timeType, false, empty), 250L);
                }
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void start() {
        network.observe(this, true);
    }

/*    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(@NonNull NetworkState eventType) {
        if (eventType == NetworkState.ONLINE) {
            Response<GraphItem> result = getOutput().getValue();
            if (result instanceof Response.Failure) {
                getEx().getUiExecutor().execute(() -> load(cmcCurrency, timeType, false));
            }
        }
    }*/

    public void load(TimeType timeType, boolean fresh) {
        boolean empty = lastResult == null || !lastResult.isSuccess();
        load(timeType, fresh, empty);
    }

    public void load(TimeType timeType, boolean important, boolean progress) {
        if (timeType == null) {
            return;
        }
        Currency currency = getCurrentCurrency();
        this.timeType = timeType;
        if (!takeAction(important, getSingleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemRx(currency))
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.GET, result);
                }, error -> {
                    if (progress) {
                        postProgress(true);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
    }

    public Currency getCurrentCurrency() {
        return pref.getGraphCurrency(Currency.USD);
    }

    public void setCurrentCurrencyCode(String currency) {
        pref.setGraphCurrency(Currency.valueOf(currency));
    }

    private Maybe<GraphItem> getItemRx(Currency currency) {
        Coin coin = Objects.requireNonNull(getTask()).getInput();
        long startTime = getStartTime(timeType);
        long endTime = TimeUtil.currentTime();
        return repo.getItemRx(coin.getSlug(), startTime, endTime).map(graph -> getItem(graph, currency));
    }

    private GraphItem getItem(Graph graph, Currency currency) {
        GraphItem item = GraphItem.getItem(graph, currency);
        List<Entry> prices = buildPrices(graph, currency);
        if (!DataUtil.isEmpty(prices)) {
            float currentPrice = getCurrentPrice(prices);
            long currentTime = getCurrentTime(prices);
            float differencePrice = getDifferencePrice(prices);
            float changeInPercent = getChangeInPercent(prices);
            LineData lineData = getLineData(prices, changeInPercent);
            int changeInPercentColor = getChangeInPercentColor(changeInPercent);
            int changeInPercentFormat = getChangeInPercentFormat(changeInPercent);
            IAxisValueFormatter formatter = getAxisValueFormatter(timeType);

            item.setLineData(lineData);
            item.setCurrentPrice(currentPrice);
            item.setCurrentTime(currentTime);
            item.setDifferencePrice(differencePrice);
            item.setChangeInPercent(changeInPercent);
            item.setChangeInPercentFormat(changeInPercentFormat);
            item.setChangeInPercentColor(changeInPercentColor);
            item.setXAxisValueFormatter(formatter);
        } else {
            item.setSuccess(false);
        }
        return item;
    }

    private long getStartTime(TimeType timeType) {
        switch (timeType) {
            case DAY:
                return TimeUtil.getPreviousDay();
            case WEEK:
                return TimeUtil.getPreviousWeek();
            case MONTH:
                return TimeUtil.getPreviousMonth();
            case THREE_MONTH:
                return TimeUtil.getPreviousMonth(3);
            case SIX_MONTH:
                return TimeUtil.getPreviousMonth(6);
            case YEAR:
                return TimeUtil.getPreviousYear();
            case ALL:
            default:
                return 0;
        }
    }

    private IAxisValueFormatter getAxisValueFormatter(TimeType timeType) {
        switch (timeType) {
            case DAY:
                return new TimeDateFormatter();
            case WEEK:
            case MONTH:
            case THREE_MONTH:
            case SIX_MONTH:
                return new MonthSlashDayDateFormatter();
            case YEAR:
            case ALL:
            default:
                return new MonthSlashYearFormatter();
        }
    }

    private List<Entry> buildPrices(Graph graph, Currency currency) {
        List<Entry> result = new ArrayList<>();
        List<List<Float>> prices = getPrices(graph, currency);
        for (List<Float> price : prices) {
            result.add(new Entry(price.get(0), price.get(1)));
        }
        return result;
    }

    private List<List<Float>> getPrices(Graph coinChart, Currency currency) {
        switch (currency) {
            case BTC:
                return coinChart.getPriceBtc();
            case USD:
            default:
                return coinChart.getPriceUsd();
        }
    }

    private LineData getLineData(List<Entry> prices, float changeInPercent) {
        Context context = getApplication();
        String priceLabel = TextUtil.getString(context, R.string.price);
        int borderColor = ColorUtil.getColor(context, getBorderColor(changeInPercent));
        int fillColor = ColorUtil.getColor(context, getFillColor(changeInPercent));

        LineDataSet data = new LineDataSet(prices, priceLabel);
        data.setColor(borderColor);
        data.setFillColor(fillColor);
        data.setDrawHighlightIndicators(true);
        data.setDrawFilled(true);
        data.setDrawCircles(true);
        data.setCircleColor(borderColor);
        data.setDrawCircleHole(false);
        data.setDrawValues(false);
        data.setCircleRadius(1);
        data.setHighlightLineWidth(2);
        data.setHighlightEnabled(true);
        data.setDrawHighlightIndicators(true);
        data.setHighLightColor(borderColor);
        return new LineData(data);
    }

    private float getCurrentPrice(List<Entry> prices) {
        float currentPrice = prices.get(prices.size() - 1).getY();
        return currentPrice;
    }

    private long getCurrentTime(List<Entry> prices) {
        long currentTime = (long) prices.get(prices.size() - 1).getX();
        return currentTime;
    }

    private float getDifferencePrice(List<Entry> prices) {
        float currentPrice = getCurrentPrice(prices);
        float startPrice = Stream.of(prices).filter(value -> value.getY() != 0.0f).findFirst().get().getY();
        float diff = currentPrice - startPrice;
        return diff;
    }

    private float getChangeInPercent(List<Entry> prices) {
        float startPrice = Stream.of(prices).filter(value -> value.getY() != 0.0f).findFirst().get().getY();
        float diff = getDifferencePrice(prices);
        float percent = (diff / startPrice) * 100;
        return percent;
    }

    @ColorRes
    private int getFillColor(float changeInPercent) {
        return changeInPercent >= 0.0f ? R.color.material_green300 : R.color.material_red300;
    }

    @ColorRes
    private int getBorderColor(float changeInPercent) {
        return changeInPercent >= 0.0f ? R.color.material_green800 : R.color.material_red800;
    }

    @StringRes
    private int getChangeInPercentFormat(float changeInPercent) {
        return changeInPercent >= 0.0f ? R.string.positive_variable_pct_change_with_dollars_format : R.string.negative_variable_pct_change_with_dollars_format;
    }

    @ColorRes
    private int getChangeInPercentColor(float changeInPercent) {
        return changeInPercent >= 0.0f ? R.color.material_green500 : R.color.material_red500;
    }

    public String getTimeTypeValue(TimeType timeType) {
        int resId;
        switch (timeType) {
            case DAY:
                resId = R.string.one_day;
                break;
            case WEEK:
                resId = R.string.week;
                break;
            case MONTH:
                resId = R.string.month;
                break;
            case THREE_MONTH:
                resId = R.string.three_month;
                break;
            case SIX_MONTH:
                resId = R.string.six_month;
                break;
            case YEAR:
                resId = R.string.year;
                break;
            case ALL:
            default:
                resId = R.string.all;
                break;
        }
        return TextUtil.getString(getApplication(), resId);
    }

    public String getFormattedPrice(Currency currency, float price) {
        return formatter.format(currency, price);
    }


}
