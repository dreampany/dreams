package com.dreampany.lca.vm;


import android.app.Application;
import com.annimon.stream.Stream;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
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

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class LiveViewModel extends BaseViewModel<Coin, CoinItem, UiTask<Coin>> {

    private static final boolean OPEN = true;

    private final NetworkManager network;
    private final Pref pref;
    private final CoinRepository repo;

    private Disposable updateDisposable;

    private SmartAdapter.Callback<CoinItem> uiCallback;

    @Inject
    LiveViewModel(Application application,
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
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        this.uiCallback = null;
        removeUpdateDisposable();
        super.clear();
    }

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

    public void setUiCallback(SmartAdapter.Callback<CoinItem> callback) {
        this.uiCallback = callback;
    }

    public void start() {
        network.observe(this::onResult, true);
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    public void refresh(boolean onlyUpdate, boolean withProgress) {
        if (onlyUpdate) {
            update(withProgress);
            return;
        }
        loads(true, withProgress);
    }

    public void loads(boolean fresh, boolean withProgress) {
        loads(Constants.Limit.COIN_DEFAULT_INDEX, fresh, withProgress);
    }

    public void loads(int index, boolean fresh, boolean withProgress) {
        if (!OPEN) {
            return;
        }
        if (!preLoads(fresh)) {
            return;
        }
        int limit = Constants.Limit.COIN_PAGE;
        Currency currency = Currency.USD;
        Disposable disposable = getRx()
                .backToMain(getListingRx(index, limit, currency))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            postProgress(false);
                            postResult(result);
                        },
                        error -> postFailureMultiple(new MultiException(error, new ExtraException()))
                );
        addMultipleSubscription(disposable);
    }

    public void update(boolean withProgress) {
        if (!OPEN) {
            return;
        }
        Timber.v("update fired");
        if (hasDisposable(updateDisposable)) {
            return;
        }
        Currency currency = Currency.USD;
        updateDisposable = getRx()
                .backToMain(getVisibleItemsIfRx(currency))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            if (!DataUtil.isEmpty(result)) {
                                postProgress(false);
                                postResult(result);
                            } else {
                                postProgress(false);
                            }
                        }, this::postFailure);
        addSubscription(updateDisposable);
    }

    /**
     * private api
     */
    private Maybe<List<CoinItem>> getListingRx(int index, int limit, Currency currency) {
        return repo
                .getItemsRx(CoinSource.CMC, index, limit, currency)
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) this::getItemsRx);
    }

    private List<CoinItem> getVisibleItemsIf(Currency currency) {
        if (uiCallback == null) {
            return null;
        }
        List<CoinItem> items = uiCallback.getVisibleItems();
        if (!DataUtil.isEmpty(items)) {
            List<String> symbols = new ArrayList<>();
            for (CoinItem item : items) {
                symbols.add(item.getItem().getSymbol());
            }
            items = null;
            if (!DataUtil.isEmpty(symbols)) {
                String[] result = DataUtil.toStringArray(symbols);
                List<Coin> coins = repo.getItems(CoinSource.CMC, result, currency);
                if (!DataUtil.isEmpty(coins)) {
                    items = getItems(coins);
                }
            }
        }
        return items;
    }

    private Maybe<List<CoinItem>> getVisibleItemsIfRx(Currency currency) {
        return Maybe.fromCallable(() -> {
            List<CoinItem> result = getVisibleItemsIf(currency);
            if (DataUtil.isEmpty(result)) {
                throw new EmptyException();
            }
            return result;
        })/*.onErrorReturn(throwable -> new ArrayList<>())*/
                .onErrorResumeNext(Maybe.empty());
    }

    @DebugLog
    private Maybe<List<CoinItem>> getItemsRx(List<Coin> result) {
        return Maybe.fromCallable(() -> getItems(result));
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

        putFlags(coins, Constants.Limit.COIN_FLAG);
        List<CoinItem> items = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            CoinItem item = getItem(coin);
            items.add(item);
        }
        Timber.v("Live Update Result in VM %d", items.size());
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

}
