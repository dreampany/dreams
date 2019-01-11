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
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.data.source.repository.CoinRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.NetworkManager;
import com.dreampany.network.data.model.Network;
import hugo.weaving.DebugLog;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private final Pref pref;
    private final CoinRepository repo;
    private SmartAdapter.Callback<CoinItem> uiCallback;
    private Disposable updateDisposable, updateUiDisposable;

    @Inject
    FlagViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  Pref pref,
                  CoinRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
        network.observe(this::onResult, true);
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        this.uiCallback = null;
        //removeUpdateItemDisposable();
        //removeUpdateVisibleItemsDisposable();
        super.clear();
    }

/*    public void removeUpdateItemDisposable() {
        removeSubscription(updateItemDisposable);
    }

    public void removeUpdateVisibleItemsDisposable() {
        removeSubscription(updateVisibleItemsDisposable);
    }*/

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

    public void refresh(boolean onlyUpdate, boolean withProgress) {
        if (onlyUpdate) {
            //updateUi();
            update();
            return;
        }
        loads(false, withProgress);
    }

    public void loads(boolean fresh, boolean withProgress) {
        if (!OPEN) {
            return;
        }
        if (!preLoads(fresh)) {
            //update();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(
                        result -> postResult(result, withProgress),
                        error -> {
                            postFailureMultiple(new MultiException(error, new ExtraException()));
                        });
        addMultipleSubscription(disposable);
        //update();
    }

    public void update() {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateDisposable)) {
            Timber.v("update Running...");
            return;
        }
        updateDisposable = getRx()
                .backToMain(getVisibleItemsIfRx())
                .subscribe(result -> postResult(result, true), this::postFailure);
        addSubscription(updateDisposable);
    }

    @DebugLog
    public void updateUi() {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateUiDisposable)) {
            Timber.v("updateUi Running...");
            return;
        }
        updateUiDisposable = getRx()
                .backToMain(getUiItemsRx())
                .subscribe(
                        result -> postResult(result, false),
                        error -> {
                            });
        addSubscription(updateUiDisposable);
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

/*    @DebugLog
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
                .subscribe(
                        result -> postResult(result, false),
                        this::postFailure);
        addSubscription(updateItemDisposable);
    }*/



    private Maybe<List<CoinItem>> getItemsRx() {
        return repo.getFlagsRx()
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) this::getItemsRx);
    }

    private Maybe<List<CoinItem>> getUiItemsRx() {
        return Maybe.fromCallable(() -> {
            List<CoinItem> items = uiCallback.getItems();
            if (!DataUtil.isEmpty(items)) {
                for (CoinItem item : items) {
                    adjustFlag(item.getItem(), item);
                }
            }
            return items;
        });
    }

    private Maybe<List<CoinItem>> getVisibleItemsIfRx() {
        return Maybe.fromCallable(this::getVisibleItemsIf);
    }

    private List<CoinItem> getVisibleItemsIf() {
        if (uiCallback == null) {
            return null;
        }
        List<CoinItem> items = uiCallback.getVisibleItems();
        if (!DataUtil.isEmpty(items)) {
            List<Long> ids = new ArrayList<>();
            for (CoinItem item : items) {
                if (needToUpdate(item.getItem())) {
                    ids.add(item.getItem().getCoinId());
                }
            }
            items = null;
            if (!ids.isEmpty()) {
                List<Coin> result = repo.getItemsByCoinIdsRx(ids).blockingGet();
                items = getItems(result);
            }
        }
        return items;
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

    private List<CoinItem> getItems(List<Coin> result) {
        List<Coin> coins = new ArrayList<>(result);
        List<Coin> ranked = new ArrayList<>();
        for (Coin coin : coins) {
            if (coin.getRank() > 0) {
                ranked.add(coin);
            }
        }
        Collections.sort(ranked, (left, right) -> left.getRank() - right.getRank());
        coins.removeAll(ranked);
        coins.addAll(0, ranked);

        List<CoinItem> items = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            CoinItem item = getItem(coin);
            items.add(item);
        }
        return items;
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

    private boolean needToUpdate(Coin coin) {
        long lastTime = pref.getCoinUpdateTime(coin.getSymbol());
        return TimeUtil.isExpired(lastTime, Constants.Time.INSTANCE.getCoin());
    }
}
