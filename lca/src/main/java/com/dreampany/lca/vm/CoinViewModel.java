package com.dreampany.lca.vm;

import android.app.Application;
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
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.R;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.misc.CurrencyFormatter;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.network.data.model.Network;
import com.mynameismidori.currencypicker.ExtendedCurrency;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinViewModel
        extends BaseViewModel<Coin, CoinItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private final NetworkManager network;
    private final Pref pref;
    private final ApiRepository repo;
    private final CurrencyFormatter formatter;
    private Disposable updateDisposable;
    private SmartAdapter.Callback<CoinItem> uiCallback;

    private final List<String> currencies;

    @Inject
    CoinViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  Pref pref,
                  ApiRepository repo,
                  CurrencyFormatter formatter) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
        this.formatter = formatter;

        currencies = Collections.synchronizedList(new ArrayList<>());

        String[] cur = TextUtil.getStringArray(application, R.array.crypto_currencies);
        if (!DataUtil.isEmpty(cur)) {
            currencies.addAll(Arrays.asList(cur));
        }
    }

    @Override
    public void clear() {
        network.deObserve(this, true);
        removeUpdateDisposable();
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
                    getEx().postToUi(() -> loads(false, empty), 250L);
                }
                //getEx().postToUi(this::update, 2000L);
            }
        }
        UiState finalState = state;
        //getEx().postToUiSmartly(() -> updateUiState(finalState));
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
        if (!preLoads(fresh)) {
            return;
        }
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(getItemsRx(currency))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.ADD, result);
                }, error -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void update(boolean withProgress) {
        if (hasDisposable(updateDisposable)) {
            return;
        }
        Currency currency = pref.getCurrency(Currency.USD);
        updateDisposable = getRx()
                .backToMain(getItemsRx(currency))
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.UPDATE, result);
                }, this::postFailure);
        addSubscription(updateDisposable);
    }

    public void toggleFavorite(Coin coin) {
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(toggleImpl(coin, currency))
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
    private Maybe<List<CoinItem>> getItemsRx(Currency currency) {
        return Maybe.create(emitter -> {
            Coin coin = Objects.requireNonNull(getTask()).getInput();
            Coin result = repo.getItemIf(CoinSource.CMC, coin.getSymbol(), currency);
            List<CoinItem> items = null;
            if (result != null) {
                getTask().setInput(result);
                items = new ArrayList<>();
                items.add(getItem(result, currency));
                items.add(getQuoteCoinItem(result, currency));
            }

            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(items)) {
                emitter.onError(new NullPointerException());
            } else {
                emitter.onSuccess(items);
            }
        });
    }

/*    private Maybe<List<CoinItem>> getItemsRx(Coin coin, Currency currency) {
        return Maybe.zip(
                getDetailsCoinItem(coin),
                getQuoteCoinItem(coin, currency),
                (left, right) -> Arrays.asList(left, right));
    }*/

    private CoinItem getQuoteCoinItem(Coin coin, Currency currency) {
        CoinItem item = CoinItem.getQuoteItem(coin, currency);
        item.setFormatter(formatter);
        adjustFavorite(coin, item);
        return item;
    }

    private Maybe<CoinItem> toggleImpl(Coin coin, Currency currency) {
        return Maybe.fromCallable(() -> {
            repo.toggleFavorite(coin);
            return getItem(coin, currency);
        });
    }

    private CoinItem getItem(Coin coin, Currency currency) {
        SmartMap<Long, CoinItem> map = getUiMap();
        CoinItem item = map.get(coin.getId());
        if (item == null) {
            item = CoinItem.getDetailsItem(coin, currency);
            item.setFormatter(formatter);
            map.put(coin.getId(), item);
        }
        item.setItem(coin);
        item.setCurrency(currency);
        adjustFavorite(coin, item);
        return item;
    }

    private void adjustFavorite(Coin coin, CoinItem item) {
        item.setFavorite(repo.isFavorite(coin));
    }
}
