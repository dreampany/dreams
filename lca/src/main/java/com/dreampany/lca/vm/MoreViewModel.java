package com.dreampany.lca.vm;

import android.app.Activity;
import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.SettingsUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.model.More;
import com.dreampany.lca.ui.enums.MoreType;
import com.dreampany.lca.ui.model.MoreItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MoreViewModel extends BaseViewModel<More, MoreItem, UiTask<More>> {

    @Inject
    MoreViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network) {
        super(application, rx, ex, rm);
    }

    @DebugLog
    public void loads(boolean fresh) {
        if (fresh) {
            removeMultipleSubscription();
        }
        if (hasMultipleDisposable()) {
            notifyUiState();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItems())
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(result -> postResult(result, true), this::postFailureMultiple);
        addMultipleSubscription(disposable);
    }

    private Flowable<List<MoreItem>> getItems() {
        return Flowable.fromCallable(() -> {
            List<MoreItem> items = new ArrayList<>();
            items.add(MoreItem.getItem(new More(MoreType.APPS)));
            items.add(MoreItem.getItem(new More(MoreType.RATE_US)));
            items.add(MoreItem.getItem(new More(MoreType.FEEDBACK)));
            //items.add(MoreItem.getItemBySymbolRx(new More(MoreType.INVITE)));
            items.add(MoreItem.getItem(new More(MoreType.SETTINGS)));
            items.add(MoreItem.getItem(new More(MoreType.LICENSE)));
            items.add(MoreItem.getItem(new More(MoreType.ABOUT)));
            return items;
        });
    }

    public void moreApps(Activity activity) {
        SettingsUtil.moreApps(Objects.requireNonNull(activity));
    }

    public void rateUs(Activity activity) {
        SettingsUtil.rateUs(activity);
    }

    public void sendFeedback(Activity activity) {
        SettingsUtil.feedback(activity);
    }
}
