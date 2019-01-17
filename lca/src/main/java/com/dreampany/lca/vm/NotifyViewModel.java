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
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Price;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.data.source.repository.CoinRepository;
import com.dreampany.lca.data.source.repository.PriceRepository;
import com.dreampany.lca.ui.activity.NavigationActivity;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.network.NetworkManager;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 7/22/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class NotifyViewModel {

    private final Application application;
    private final RxMapper rx;
    private final CoinRepository repo;
    private final PriceRepository priceRepo;
    private final NotifyManager notify;
    private Disposable disposable;

    private final Map<Coin, Price> prices;

    @Inject
    NotifyViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    NetworkManager network,
                    CoinRepository repo,
                    PriceRepository priceRepo) {
        this.application = application;
        this.rx = rx;
        this.repo = repo;
        this.priceRepo = priceRepo;
        this.notify = new NotifyManager();
        prices = Maps.newConcurrentMap();
    }

    public void clear() {
        if (hasDisposable()) {
            disposable.dispose();
        }
    }

    @DebugLog
    public void notifyIf() {
        if (hasDisposable()) {
            //return;
        }
        Timber.v("Processing");
        this.disposable = rx.backToMain(getProfitableItemsRx())
                .subscribe(this::postResult, this::postFailed);
    }

    private Maybe<List<CoinItem>> getProfitableItemsRx() {
        return repo.getItemsRx(CoinSource.CMC)
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) this::getProfitableItemsRx);
    }

    @DebugLog
    private Maybe<List<CoinItem>> getProfitableItemsRx(List<Coin> result) {
        return Flowable.fromIterable(result)
                .filter(this::isProfitable)
                .map(CoinItem::getSimpleItem).toList().toMaybe();
    }

    private boolean hasDisposable() {
        return disposable != null && !disposable.isDisposed();
    }

    @DebugLog
    private void postResult(List<CoinItem> items) {
        App app = (App) application;
        if (app.isVisible()) {
            //return;
        }

        String title = TextUtil.getString(application, R.string.app_name);
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
            message = TextUtil.getString(app, R.string.profitable_coin, coin.getSymbol(), coin.getName(), coin.getUsdQuote().getDayChange());
        } else {
            message = TextUtil.getString(app, R.string.profitable_coins_motto);
        }
        notify.showNotification(application, title, message, NavigationActivity.class);
    }

    @DebugLog
    private void postFailed(Throwable error) {

    }

    @DebugLog
    private boolean isProfitable(Coin coin) {
        Quote quote = coin.getUsdQuote();
        return quote.getDayChange() >= 0;
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
