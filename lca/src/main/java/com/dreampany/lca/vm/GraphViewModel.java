package com.dreampany.lca.vm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.util.ColorUtil;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.R;
import com.dreampany.lca.api.cmc.enums.Currency;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.data.source.repository.GraphRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.misc.CurrencyFormatter;
import com.dreampany.lca.misc.MonthSlashDayDateFormatter;
import com.dreampany.lca.misc.MonthSlashYearFormatter;
import com.dreampany.lca.misc.TimeDateFormatter;
import com.dreampany.lca.ui.enums.TimeType;
import com.dreampany.lca.ui.model.GraphItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.NetworkManager;
import com.dreampany.network.data.model.Network;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class GraphViewModel extends BaseViewModel<Graph, GraphItem, UiTask<Coin>> {

    private final NetworkManager network;
    private final GraphRepository repo;
    private CurrencyFormatter formatter;
    private Currency currency;
    private TimeType timeType;

    @Inject
    GraphViewModel(Application application,
                   RxMapper rx,
                   AppExecutors ex,
                   ResponseMapper rm,
                   NetworkManager network,
                   CurrencyFormatter formatter,
                   GraphRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        this.formatter = formatter;
        network.observe(this::onResult, true);
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        super.clear();
    }

    @DebugLog
    void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.isConnected()) {
                state = UiState.ONLINE;
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

/*    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(@NonNull NetworkState event) {
        if (event == NetworkState.ONLINE) {
            Response<GraphItem> result = getOutput().getValue();
            if (result instanceof Response.Failure) {
                getEx().getUiExecutor().execute(() -> load(currency, timeType, false));
            }
        }
    }*/

    @DebugLog
    public void load(Currency currency, TimeType timeType, boolean fresh) {
        this.currency = currency;
        this.timeType = timeType;
        if (fresh) {
            removeSingleSubscription();
        }
        if (hasSingleDisposable()) {
            notifyUiState();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemRx())
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(result -> postResult(result, true), error -> {
                    postFailure(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
    }

    private Maybe<GraphItem> getItemRx() {
        Coin coin = Objects.requireNonNull(getTask()).getInput();
        long startTime = getStartTime(timeType);
        long endTime = TimeUtil.currentTime();
        return repo.getItemRx(coin.getSlug(), startTime, endTime).map(this::getItem);
    }

    private GraphItem getItem(Graph graph) {
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

    private List<Entry> buildPrices(Graph coinChart, Currency currency) {
        List<Entry> result = new ArrayList<>();
        List<List<Float>> prices = getPrices(coinChart, currency);
        for (List<Float> price : prices) {
            result.add(new Entry(price.get(0), price.get(1)));
        }
        return result;
    }

    private List<List<Float>> getPrices(Graph coinChart, Currency currency) {
        switch (currency) {
            case BTC:
                return coinChart.getPriceBTC();
            case USD:
            default:
                return coinChart.getPriceUSD();
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

    public void openSourceSite(Activity activity) {
        Coin coin = getTask().getInput();
        String webUrl = Constants.Api.CoinMarketCapSiteUrl;
        String url = String.format(webUrl, coin.getSlug());
        new FinestWebView.Builder(activity).show(url);
    }
}
