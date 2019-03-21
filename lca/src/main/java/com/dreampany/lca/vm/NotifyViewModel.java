package com.dreampany.lca.vm;

import android.app.Application;
import com.dreampany.frame.api.notify.NotifyManager;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.app.App;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.*;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.data.source.repository.CoinAlertRepository;
import com.dreampany.lca.data.source.repository.PriceRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.activity.NavigationActivity;
import com.dreampany.lca.ui.model.CoinAlertItem;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.network.manager.NetworkManager;
import com.google.common.collect.Maps;
import hugo.weaving.DebugLog;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 7/22/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class NotifyViewModel {

    private final Application application;
    private final RxMapper rx;
    private final ApiRepository repo;
    private final PriceRepository priceRepo;
    private final CoinAlertRepository alertRepo;
    private final NotifyManager notify;
    //private CompositeDisposable disposables;

    private final Map<Coin, Price> prices;

    private boolean switching;

    @Inject
    NotifyViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    NetworkManager network,
                    ApiRepository repo,
                    PriceRepository priceRepo,
                    CoinAlertRepository alertRepo) {
        this.application = application;
        this.rx = rx;
        this.repo = repo;
        this.priceRepo = priceRepo;
        this.alertRepo = alertRepo;
        this.notify = new NotifyManager(application);
        prices = Maps.newConcurrentMap();
        //disposables = new CompositeDisposable();
    }

    public void clear() {
        //disposables.dispose();
    }

    public void notifyIf() {
/*        if (hasDisposable()) {
            //return;
        }*/
        Timber.v("notifyIf Processing");
        Currency currency = Currency.USD;
        if (switching) {
            Maybe<List<CoinItem>> maybe = getProfitableItemsRx(currency);
            if (maybe != null) {
                rx
                        .backToMain(maybe)
                        .subscribe(this::postResultCoins, this::postFailed);
            } else {
                Timber.e("getProfitableItemsRx is Null");
            }
        } else {
            Maybe<List<CoinAlertItem>> maybe = getAlertItemsRx();
            if (maybe != null) {
                rx
                        .backToMain(maybe)
                        .subscribe(this::postResultAlerts, this::postFailed);
            } else {
                Timber.e("getAlertItemsRx is Null");
            }
        }
        switching = !switching;

    }

    private Maybe<List<CoinItem>> getProfitableItemsRx(Currency currency) {
        int listStart = Constants.Limit.COIN_START_INDEX;
        int listLimit = Constants.Limit.COIN_PAGE;
        return repo.getItemsIfRx(CoinSource.CMC, listStart, listLimit, currency)
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) this::getProfitableItemsRx);
    }

    private Maybe<List<CoinAlertItem>> getAlertItemsRx() {
        return alertRepo.getItemsRx()
                .flatMap((Function<List<CoinAlert>, MaybeSource<List<CoinAlertItem>>>) this::getAlertItemsRx);
    }

    private Maybe<List<CoinItem>> getProfitableItemsRx(List<Coin> result) {
        return Flowable.fromIterable(result)
                .filter(this::isProfitable)
                .map(coin -> CoinItem.getSimpleItem(coin, Currency.USD))
                .toList()
                .toMaybe();
    }

    private Maybe<List<CoinAlertItem>> getAlertItemsRx(List<CoinAlert> result) {
        return Flowable.fromIterable(result)
                .filter(this::isAlertable)
                .map(alert -> {
                    Coin coin = repo.getItemIf(CoinSource.CMC, alert.getSymbol(), Currency.USD);
                    return CoinAlertItem.getItem(coin, alert);
                }).toList()
                .toMaybe();
    }

/*    private boolean hasDisposable() {
        return disposable != null && !disposable.isDisposed();
    }*/

    @DebugLog
    private void postResultCoins(List<CoinItem> items) {
        App app = (App) application;
        if (app.isVisible()) {
            //return;
        }

        String message;
        if (!DataUtil.isEmpty(items)) {
            CoinItem profitable = items.get(0);
            for (int index = 1; index < items.size(); index++) {
                if (items.get(index).getItem().getUsdQuote().getDayChange() >
                        profitable.getItem().getUsdQuote().getDayChange()) {
                    profitable = items.get(index);
                }
            }
            Coin coin = profitable.getItem();
            message = TextUtil.getString(app,
                    R.string.profitable_coin,
                    coin.getSymbol(),
                    coin.getName(),
                    coin.getUsdQuote().getPrice(),
                    coin.getUsdQuote().getDayChange());
        } else {
            message = TextUtil.getString(app, R.string.profitable_coins_motto);
        }
        notify.showNotification(message, R.drawable.ic_notification, NavigationActivity.class);
    }

    @DebugLog
    private void postResultAlerts(List<CoinAlertItem> items) {
        if (DataUtil.isEmpty(items)) {
            return;
        }
        StringBuilder message = new StringBuilder();
        for (CoinAlertItem item : items) {
            if (message.length() > 0) {
                message.append(Constants.Sep.COMMA_SPACE);
            }
            Coin coin = item.getCoin();
            CoinAlert alert = item.getItem();
            message.append(item.getCoin().getSymbol());
            if (alert.hasPriceUp()) {
                message
                        .append(Constants.Sep.SPACE)
                        .append(Constants.Sep.UP)
                        .append(alert.getPriceUp());
            }
            if (alert.hasPriceDown()) {
                message
                        .append(Constants.Sep.SPACE)
                        .append(Constants.Sep.DOWN)
                        .append(alert.getPriceDown());
            }
        }
        notify.showNotification(
                message.toString(),
                R.drawable.ic_notification,
                Constants.Notify.ALERT_ID,
                Constants.Notify.ALERT_CHANNEL_ID,
                NavigationActivity.class);
    }

    @DebugLog
    private void postFailed(Throwable error) {

    }

    private boolean isProfitable(Coin coin) {
        Quote quote = coin.getUsdQuote();
        return quote.getDayChange() >= 0;
    }

    private boolean isAlertable(CoinAlert alert) {
        Coin coin = repo.getItemIf(CoinSource.CMC, alert.getSymbol(), Currency.USD);
        if (coin == null) {
            return false;
        }
        Quote quote = coin.getUsdQuote();
        if (alert.hasPriceUp() && quote.getPrice() > alert.getPriceUp()) {
            return true;
        }
        if (alert.hasPriceDown() && quote.getPrice() > alert.getPriceDown()) {
            return true;
        }
        return false;
    }

/*    @DebugLog
    private boolean isProfitable(Coin coin) {
        CmcQuote priceQuote = coin.getUsdPriceQuote();
        if (!prices.containsKey(coin)) {
            Price price = new Price(coin.getId(), priceQuote.getPrice());
            prices.put(coin, price);
        }
        Price exists = prices.get(coin);
        boolean profitable = priceQuote.getPrice() >= exists.getPrice();
        exists.setPrice(priceQuote.getPrice());
        return profitable;
    }*/
}
