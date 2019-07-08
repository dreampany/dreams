package com.dreampany.lca.vm;


import android.app.Application;

import androidx.annotation.NonNull;

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
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.R;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.data.source.repository.CoinRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.misc.CurrencyFormatter;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.manager.NetworkManager;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

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
public class CoinsViewModel
        extends BaseViewModel<Coin, CoinItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private final NetworkManager network;
    private final Pref pref;
    private final ApiRepository repo;
    private final CoinRepository coinRepo;
    private final CurrencyFormatter formatter;
    private SmartAdapter.Callback<CoinItem> uiCallback;

    private final List<String> currencies;
    //private Currency currentCurrency;

    @Inject
    CoinsViewModel(@NotNull Application application,
                   @NotNull RxMapper rx,
                   @NotNull AppExecutors ex,
                   @NotNull ResponseMapper rm,
                   NetworkManager network,
                   Pref pref,
                   ApiRepository repo,
                   CoinRepository coinRepo,
                   CurrencyFormatter formatter) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
        this.coinRepo = coinRepo;
        this.formatter = formatter;
        currencies = Collections.synchronizedList(new ArrayList<>());

        String[] cur = TextUtil.getStringArray(application, R.array.crypto_currencies);
        if (!DataUtil.isEmpty(cur)) {
            currencies.addAll(Arrays.asList(cur));
        }
    }

    @Override
    public void clear() {
        network.deObserve(this);
        this.uiCallback = null;
        super.clear();
    }

    @Override
    public void onNetworkResult(@NonNull List<Network> networks) {
        Timber.v("onNetworkResult %d", networks.size());
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.getInternet()) {
                state = UiState.ONLINE;
                Response<List<CoinItem>> result = getOutputs().getValue();
                if (result == null || result instanceof Response.Failure) {
                    boolean empty = uiCallback == null || uiCallback.getEmpty();
                    getEx().postToUi(() -> loads(0, false, empty), 250L);
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

    public void refresh(boolean update, boolean important, boolean progress) {
        Timber.v("refresh fired");
        if (update) {
            update(important, progress);
            return;
        }
        loads(important, progress);
    }

    public void loads(boolean important, boolean progress) {
        Timber.v("loads fired");
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Currency currency = pref.getCurrency(Currency.USD);
        Timber.v("loads fired for %s", currency.name());
        Disposable disposable = getRx()
                .backToMain(getListingRx(currency))
                .doOnSubscribe(subscription -> {
                    if (!pref.isLoaded()) {
                        updateUiState(UiState.NONE);
                    }
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    if (!DataUtil.isEmpty(result)) {
                        pref.commitLoaded();
                    }
                    Timber.v("Result posting %d", result.size());
                    postResult(Response.Type.GET, result);
                    getEx().postToUi(() -> update(false, false), 3000L);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void loads(int index, boolean important, boolean progress) {
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(getListingRx(currency, index))
                .doOnSubscribe(subscription -> {
                    if (!pref.isLoaded()) {
                        updateUiState(UiState.NONE);
                    }
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    if (!DataUtil.isEmpty(result)) {
                        pref.commitLoaded();
                    }                    Timber.v("Result posting %d", result.size());
                    postResult(Response.Type.GET, result);
                    //getEx().postToUi(() -> update(false), 2000L);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void update(boolean important, boolean progress) {
        Timber.v("update fired");
        if (!takeAction(important, getSingleDisposable())) {
            return;
        }
        Timber.v("update fired accepted");
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(getUpdateItemsIfRx(currency))
                .doOnSubscribe(subscription -> {
                    if (!pref.isLoaded()) {
                        updateUiState(UiState.NONE);
                    }
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    if (!DataUtil.isEmpty(result)) {
                        pref.commitLoaded();
                    }
                    postResult(Response.Type.UPDATE, result);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void toggleFavorite(Coin coin) {
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(toggleImpl(currency, coin))
                .subscribe(result -> postResult(Response.Type.UPDATE, result, false), this::postFailure);
    }

    public String getCurrentCurrencyCode() {
        return pref.getCurrency(Currency.USD).name();
    }

    public Currency getCurrentCurrency() {
        return pref.getCurrency(Currency.USD);
    }

    public void setCurrentCurrencyCode(String currency) {
        pref.setCurrency(Currency.valueOf(currency));
    }

    public List<ExtendedCurrency> getCurrencies() {
        List<ExtendedCurrency> result = new ArrayList<>();
        for (ExtendedCurrency currency : ExtendedCurrency.CURRENCIES) {
            if (currencies.contains(currency.getCode())) {
                result.add(currency);
            }
        }
        return result;
    }

    /* private api */
    private Maybe<List<CoinItem>> getListingRx(Currency currency) {
        Timber.v("getListingRx fired for %s", currency.name());
        return coinRepo
                .getItemsRx(CoinSource.CMC, currency, Constants.Limit.COIN_FULL)
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) coins -> getItemsRx(currency, coins));
    }

    private Maybe<List<CoinItem>> getListingRx(Currency currency, int index) {
        return repo
                .getItemsIfRx(CoinSource.CMC, currency, index, Constants.Limit.COIN_PAGE)
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) coins -> getItemsRx(currency, coins));
    }

    private List<CoinItem> getUpdateItemsIf(Currency currency) {
        if (uiCallback == null) {
            return null;
        }
        //List<CoinItem> items = currency.equals(currentCurrency) ? uiCallback.getVisibleItems() : uiCallback.getItems();
        List<CoinItem> items = uiCallback.getVisibleItems();
        if (!DataUtil.isEmpty(items)) {
            List<String> coinIds = new ArrayList<>();
            for (CoinItem item : items) {
                coinIds.add(item.getItem().getId());
            }
            items = null;
            if (!DataUtil.isEmpty(coinIds)) {
                List<Coin> coins = repo.getItemsIf(CoinSource.CMC, currency, coinIds);
                if (!DataUtil.isEmpty(coins)) {
                    items = getItems(currency, coins);
                    for (Coin item : coins) {
                        Timber.v("%s = %s", item.getName(), TimeUtil.getFullTime(item.getLastUpdated()));
                    }
                }
            }
        }
        return items;
    }

/*    private List<CoinItem> getUpdateItemsIf(Currency currency) {
        if (uiCallback == null) {
            return null;
        }
        List<CoinItem> items = currency.equals(currency) ? uiCallback.getVisibleItems() : uiCallback.getItemsWithoutId();
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
                    items = getItemsWithoutId(coins, currency);
                }
            }
        }
        return items;
    }*/

    private Maybe<List<CoinItem>> getUpdateItemsIfRx(Currency currency) {
        return Maybe.create(emitter -> {
            List<CoinItem> result = getUpdateItemsIf(currency);
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

    private Maybe<List<CoinItem>> getItemsRx(Currency currency, List<Coin> items) {
        return Maybe
                .create(emitter -> {
                    Timber.v("Input Coins %d", items.size());
                    List<CoinItem> result = getItems(currency, items);
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

    private Maybe<CoinItem> toggleImpl(Currency currency, Coin coin) {
        return Maybe.fromCallable(() -> {
            repo.toggleFavorite(coin);
            return getItem(currency, coin);
        });
    }

    private List<CoinItem> getItems(Currency currency, List<Coin> result) {
        List<Coin> coins = new ArrayList<>(result);
        List<Coin> ranked = new ArrayList<>();
        for (Coin coin : coins) {
            if (coin.getRank() > 0) {
                ranked.add(coin);
            }
        }
        Timber.v("Coins Result %d = Clone %d", coins.size(), ranked.size());
        Timber.v("Coins Preparing ranked %d", ranked.size());
        Collections.sort(ranked, (left, right) -> left.getRank() - right.getRank());
        coins.removeAll(ranked);
        coins.addAll(0, ranked);
        Timber.v("Coins Preparing sorted %d", ranked.size());
        //putFlags(coins, Constants.Limit.COIN_FLAG);
        List<CoinItem> items = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            CoinItem item = getItem(currency, coin);
            items.add(item);
            Timber.v("Coin %s %s", item.getItem().getName(), item.getItem().getId());
        }
        return items;
    }

    private CoinItem getItem(Currency currency, Coin coin) {
        SmartMap<String, CoinItem> map = getUiMap();
        CoinItem item = map.get(coin.getId());
        if (item == null) {
            item = CoinItem.getSimpleItem(coin, currency);
            item.setFormatter(formatter);
            map.put(coin.getId(), item);
        }
        item.setItem(coin);
        item.setCurrency(currency);
        adjustFavorite(coin, item);
        adjustAlert(coin, item);
        return item;
    }

    private void adjustFavorite(Coin coin, CoinItem item) {
        item.setFavorite(repo.isFavorite(coin));
    }

    private void adjustAlert(Coin coin, CoinItem item) {
        item.setAlert(repo.hasAlert(coin));
    }

}
