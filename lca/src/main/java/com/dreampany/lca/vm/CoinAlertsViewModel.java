package com.dreampany.lca.vm;

import android.app.Application;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.repository.CoinAlertRepository;
import com.dreampany.lca.data.source.repository.CoinRepository;
import com.dreampany.lca.ui.model.CoinAlertItem;
import com.dreampany.lca.ui.model.UiTask;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Roman-372 on 3/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class CoinAlertsViewModel
        extends BaseViewModel<CoinAlert, CoinAlertItem, UiTask<CoinAlert>> {

    private CoinRepository coinRepo;
    private CoinAlertRepository alertRepo;
    private SmartAdapter.Callback<CoinAlertItem> uiCallback;

    @Inject
    CoinAlertsViewModel(Application application,
                        RxMapper rx,
                        AppExecutors ex,
                        ResponseMapper rm,
                        CoinRepository coinRepo,
                        CoinAlertRepository alertRepo) {
        super(application, rx, ex, rm);
        this.coinRepo = coinRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public void clear() {
        this.uiCallback = null;
        super.clear();
    }

    public void setUiCallback(SmartAdapter.Callback<CoinAlertItem> callback) {
        this.uiCallback = callback;
    }

    public void loads(boolean fresh, boolean withProgress) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getAlertsRx())
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(result);
                }, error -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    /* internal api */
    private Maybe<List<CoinAlertItem>> getAlertsRx() {
        return alertRepo.getItemsRx()
                .flatMap((Function<List<CoinAlert>, MaybeSource<List<CoinAlertItem>>>) this::getItemsRx);
    }

    private Maybe<List<CoinAlertItem>> getItemsRx(List<CoinAlert> result) {
        return Flowable.fromIterable(result)
                .map(alert -> {
                    Coin coin = coinRepo.getItem(CoinSource.CMC, alert.getSymbol(), Currency.USD);
                    return getItem(coin, alert);
                })
                .toList()
                .toMaybe();
    }

    private CoinAlertItem getItem(Coin coin, CoinAlert alert) {
        SmartMap<Long, CoinAlertItem> map = getUiMap();
        CoinAlertItem item = map.get(alert.getId());
        if (item == null) {
            item = CoinAlertItem.getItem(coin, alert);
            map.put(alert.getId(), item);
        }
        item.setItem(alert);
        return item;
    }
}
