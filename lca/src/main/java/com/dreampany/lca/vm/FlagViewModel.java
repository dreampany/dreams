package com.dreampany.lca.vm;


import android.app.Application;

import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.source.repository.CoinRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.NetworkManager;
import com.dreampany.network.data.model.Network;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class FlagViewModel extends BaseViewModel<Coin, CoinItem, UiTask<Coin>> {

    private static final boolean OPEN = true;

    private static final long initialDelay = 0L;
    private static final long period = Constants.Period.INSTANCE.getCoin();
    private static final int retry = 3;

    private final NetworkManager network;
    private final CoinRepository repo;
    private SmartAdapter.Callback<CoinItem> uiCallback;
    private Disposable updateItemDisposable, updateVisibleItemsDisposable;

    @Inject
    FlagViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  CoinRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        network.observe(this::onResult, true);
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        this.uiCallback = null;
        removeUpdateItemDisposable();
        removeUpdateVisibleItemsDisposable();
        super.clear();
    }

    public void removeUpdateItemDisposable() {
        removeSubscription(updateItemDisposable);
    }

    public void removeUpdateVisibleItemsDisposable() {
        removeSubscription(updateVisibleItemsDisposable);
    }

    public void setUiCallback(SmartAdapter.Callback<CoinItem> callback) {
        this.uiCallback = callback;
    }

    @DebugLog
    void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.isConnected()) {
                state = UiState.ONLINE;
                //getEx().postToUi(this::updateItem, 2000L);
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void loads(boolean fresh) {
        if (!OPEN) {
            return;
        }
        if (!preLoads(fresh)) {
            updateVisibleItems();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(this::postResultWithProgress, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
        updateVisibleItems();
    }

    public void toggle(Coin coin) {
        if (!OPEN) {
            return;
        }
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(coin))
                .subscribe(this::postFlag, this::postFailure);
        addSingleSubscription(disposable);
    }

    @DebugLog
    public void updateItem() {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateItemDisposable )) {
            Timber.v("Updater Running...");
            return;
        }
        updateItemDisposable = getRx()
                .backToMain(updateItemInterval())
                .subscribe(this::postResult, this::postFailure);
        addSubscription(updateItemDisposable);
    }

    public void updateVisibleItems() {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateVisibleItemsDisposable )) {
            Timber.v("updateVisibleItems Running...");
            return;
        }
        updateVisibleItemsDisposable = getRx()
                .backToMain(getVisibleItemsRx())
                .subscribe(this::postResult, error -> {

                });
        addSubscription(updateVisibleItemsDisposable);
    }

    private Maybe<List<CoinItem>> getItemsRx() {
        return repo.getFlagsRx()
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) this::getItemsRx);
    }

    private Maybe<List<CoinItem>> getVisibleItemsRx() {
        return Maybe.fromCallable(() -> {
            List<CoinItem> items = uiCallback.getVisibleItems();
            if (!DataUtil.isEmpty(items)) {
                for (CoinItem item : items) {
                    adjustFlag(item.getItem(), item);
                }
            }
            return items;
        });
    }

    private Flowable<CoinItem> updateItemInterval() {
        Flowable<CoinItem> flowable = Flowable
                .interval(initialDelay, period, TimeUnit.MILLISECONDS, getRx().io())
                .map(tick -> {
                    if (uiCallback != null) {
                        List<CoinItem> items = uiCallback.getVisibleItems();
                        if (!DataUtil.isEmpty(items)) {
                            Collections.sort(items, (left, right) -> (int) (left.getItem().getLastUpdated() - right.getItem().getLastUpdated()));
                            CoinItem item = items.get(0);
                            Timber.d("Next Item to update %s", item.getItem().getName());
                            if (TimeUtil.isExpired(item.getItem().getTime(), Constants.Time.INSTANCE.getCoin())) {
                                return updateItemRx(Objects.requireNonNull(item.getItem())).blockingGet();
                            }
                        }
                    }
                    return null;
                }).retry(retry);
        return flowable;
    }

    @DebugLog
    private Maybe<CoinItem> updateItemRx(Coin item) {
        return repo.getItemByCoinIdRx(item.getCoinId(), true).map(this::getItem);
    }

    @DebugLog
    private Maybe<List<CoinItem>> getItemsRx(List<Coin> items) {
        return Flowable.fromIterable(items)
                .map(this::getItem)
                .toList()
                .toMaybe();
    }

    private CoinItem getItem(Coin coin) {
        SmartMap<Long, CoinItem> map = getUiMap();
        CoinItem item = map.get(coin.getId());
        if (item == null) {
            item = CoinItem.getSimpleItem(coin);
            map.put(coin.getId(), item);
        }
        item.setItem(coin);
        adjustFlag(coin, item);
        return item;
    }

    private void adjustFlag(Coin coin, CoinItem item) {
        boolean flagged = repo.isFlagged(coin);
        item.setFlagged(flagged);
    }

    //todo need to improve for flowable and completable working
    private Flowable<CoinItem> toggleImpl(Coin coin) {
        return Flowable.fromCallable(() -> {
            repo.toggleFlag(coin);
            CoinItem item = getItem(coin);
            return item;
        });
    }
}
