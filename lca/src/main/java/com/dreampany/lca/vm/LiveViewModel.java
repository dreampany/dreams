package com.dreampany.lca.vm;


import android.app.Application;
import com.annimon.stream.Stream;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
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
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.source.pref.LoadPref;
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
public class LiveViewModel extends BaseViewModel<Coin, CoinItem, UiTask<Coin>> {

    private static final boolean OPEN = true;

    private static final long initialDelay = 0L;
    private static final long period = Constants.Period.INSTANCE.getCoin();
    private static final int retry = 3;


    private final NetworkManager network;
    private final Pref pref;
    private final LoadPref loadPref;
    private final CoinRepository repo;
    private SmartAdapter.Callback<CoinItem> uiCallback;
    private Disposable updateDisposable;

    @Inject
    LiveViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  Pref pref,
                  LoadPref loadPref,
                  CoinRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.loadPref = loadPref;
        this.repo = repo;
        network.observe(this::onResult, true);
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        this.uiCallback = null;
        //removeUpdateUiDisposable();
        //removeUpdateVisibleItemsDisposable();
        //removeUpdateDisposable();
        //removeUpdateItemDisposable();
        super.clear();
    }

/*    public void removeUpdateUiDisposable() {
        removeSubscription(updateUiDisposable);
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    public void removeUpdateItemDisposable() {
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
                Response<List<CoinItem>> result = getOutputs().getValue();
                if (result instanceof Response.Failure) {
                    getEx().postToUi(() -> loads(false, false), 250L);
                }
                //getEx().postToUi(this::updateItem, 2000L);
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void refresh(boolean onlyUpdate, boolean withProgress) {
        if (onlyUpdate) {
            update();
            return;
        }
        loads(false, withProgress);
    }

    @DebugLog
    public void loads(boolean fresh, boolean withProgress) {
        loads(Constants.Limit.COIN_DEFAULT_START, fresh, withProgress);
    }

    @DebugLog
    public void loads(int start, boolean fresh, boolean withProgress) {
        if (!OPEN) {
            return;
        }
        if (!preLoads(fresh)) {
            return;
        }
        int limit = Constants.Limit.COIN_PAGE;
        Disposable disposable = getRx()
                .backToMain(getListingRx(start, limit))
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(
                        result -> postResult(result, withProgress),
                        error -> {
                            postFailureMultiple(new MultiException(error, new ExtraException()));
                        });
        addMultipleSubscription(disposable);
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
                .subscribe(result -> postResult(result, false), this::postFailure);
        addSubscription(updateDisposable);
    }

/*    public void updateUi() {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateUiDisposable)) {
            Timber.v("updateUi Running...");
            return;
        }
        updateUiDisposable = getRx()
                .backToMain(getUiItemsRx())
                .subscribe(this::postResult, error -> {

                });
        addSubscription(updateUiDisposable);
    }*/


/*    @DebugLog
    public void update() {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateDisposable)) {
            Timber.v("Updater Running...");
            return;
        }
        updateDisposable = getRx()
                .backToMain(updateInterval())
                .subscribe(this::postResult, this::postFailure);
        addSubscription(updateDisposable);
    }*/

/*    @DebugLog
    public void updateItem() {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateItemDisposable)) {
            Timber.v("Updater Running...");
            return;
        }
        updateItemDisposable = getRx()
                .backToMain(updateItemInterval())
                .subscribe(this::postResult, this::postFailure);
        addSubscription(updateItemDisposable);
    }*/

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

    private Maybe<List<CoinItem>> getListingRx(int start, int limit) {
        return repo.getListingRx(CoinSource.CMC, start, limit)
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) this::getItemsRx);
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

    private Maybe<List<CoinItem>> getVisibleItemsIfRx() {
        return Maybe.fromCallable(this::getVisibleItemsIf);
    }

    private Flowable<List<CoinItem>> updateVisibleItemsIntervalRx() {
        Flowable<List<CoinItem>> flowable = Flowable
                .interval(initialDelay, period, TimeUnit.MILLISECONDS, getRx().io())
                .map(tick -> getVisibleItemsIf()).retry(retry);
        return flowable;
    }

    private Maybe<List<CoinItem>> updateInterval() {
        return repo.getRemoteListingIfRx(CoinSource.CMC)
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) this::getItemsRx);
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

    private Maybe<List<CoinItem>> getItemsRx(List<Coin> result) {
        return Maybe.fromCallable(() -> getItems(result));
    }

    @DebugLog
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

        putFlags(coins, Constants.Limit.COIN_FLAG);
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
            return getItem(coin);
        });
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

    private void putFlags(List<Coin> coins, int flagCount) {
        if (!pref.isDefaultFlagCommitted()) {
            List<Coin> flagItems = DataUtil.sub(coins, flagCount);
            if (!DataUtil.isEmpty(flagItems)) {
                Stream.of(flagItems).forEach(repo::putFlag);
                pref.commitDefaultFlag();
            }
        }
    }

    private boolean needToUpdate(Coin coin) {
        long lastTime = pref.getCoinUpdateTime(coin.getSymbol());
        return TimeUtil.isExpired(lastTime, Constants.Time.INSTANCE.getCoin());
    }

}
