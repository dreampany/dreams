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
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.manager.NetworkManager;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
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
public class CoinsViewModel
        extends BaseViewModel<Coin, CoinItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private final NetworkManager network;
    private final Pref pref;
    private final ApiRepository repo;
    private Disposable updateDisposable;
    private SmartAdapter.Callback<CoinItem> uiCallback;

    private int currentIndex;

    @Inject
    CoinsViewModel(Application application,
                   RxMapper rx,
                   AppExecutors ex,
                   ResponseMapper rm,
                   NetworkManager network,
                   Pref pref,
                   ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
        currentIndex = Constants.Limit.COIN_DEFAULT_INDEX;
    }

    @Override
    public void clear() {
        network.deObserve(this, true);
        this.uiCallback = null;
        removeUpdateDisposable();
        currentIndex = Constants.Limit.COIN_DEFAULT_INDEX;
        super.clear();
    }

    @Override
    public void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.hasInternet()) {
                state = UiState.ONLINE;
                Response<List<CoinItem>> result = getOutputs().getValue();
                if (result == null || result instanceof Response.Failure) {
                    boolean empty = uiCallback == null || uiCallback.getEmpty();
                    getEx().postToUi(() -> loads(currentIndex, false, empty), 250L);
                }
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void setUiCallback(SmartAdapter.Callback<CoinItem> callback) {
        this.uiCallback = callback;
    }

    public void start() {
        network.observe(this, true);
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
        loads(currentIndex, fresh, withProgress);
    }

    public void loads(int index, boolean fresh, boolean withProgress) {
        if (!preLoads(fresh)) {
            return;
        }
        currentIndex = index;
        int limit = Constants.Limit.COIN_PAGE;
        Currency currency = Currency.USD;
        Disposable disposable = getRx()
                .backToMain(getListingRx(index, limit, currency))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.GET, result);
                }, error -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void update(boolean withProgress) {
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
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.UPDATE, result);
                }, error -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addSubscription(updateDisposable);
    }

    /* private api */
    private Maybe<List<CoinItem>> getListingRx(int index, int limit, Currency currency) {
        return repo
                .getItemsIfRx(CoinSource.CMC, index, limit, currency)
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
                List<Coin> coins = repo.getItemsIf(CoinSource.CMC, result, currency);
                if (!DataUtil.isEmpty(coins)) {
                    items = getItems(coins);
                }
            }
        }
        return items;
    }

    private Maybe<List<CoinItem>> getVisibleItemsIfRx(Currency currency) {
        return Maybe.create(emitter -> {
            List<CoinItem> result = getVisibleItemsIf(currency);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

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

        //putFlags(coins, Constants.Limit.COIN_FLAG);
        List<CoinItem> items = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            CoinItem item = getItem(coin);
            items.add(item);
        }
        return items;
    }

    private void adjustFlag(Coin coin, CoinItem item) {
        boolean flagged = repo.isFavorite(coin);
        item.setFavorite(flagged);
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

      /*    private Maybe<CoinItem> toggleImpl(Coin coin) {
        return Maybe.fromCallable(() -> {
            repo.toggleFavorite(coin);
            return getItem(coin);
        });
    }*/

/*    private void putFlags(List<Coin> coins, int flagCount) {
        if (!pref.isDefaultFlagCommitted()) {
            List<Coin> flagItems = DataUtil.sub(coins, flagCount);
            if (!DataUtil.isEmpty(flagItems)) {
                Stream.of(flagItems).forEach(repo::putFavorite);
                pref.commitDefaultFlag();
            }
        }
    }*/

}
