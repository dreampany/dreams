package com.dreampany.demo.vm;

import android.app.Application;

import com.dreampany.demo.app.App;
import com.dreampany.demo.ui.model.DemoItem;
import com.dreampany.frame.api.notify.NotifyManager;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.network.NetworkManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
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
    private final NotifyManager notify;
    private Disposable disposable;

    @Inject
    NotifyViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    NetworkManager network) {
        this.application = application;
        this.rx = rx;
        this.notify = new NotifyManager();
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
        this.disposable = rx.backToMain(getDemoItems())
                .subscribe(this::postResult, this::postFailed);
    }

    private Flowable<List<DemoItem>> getDemoItems() {
        return null;
    }


    private boolean hasDisposable() {
        return disposable != null && !disposable.isDisposed();
    }

    @DebugLog
    private void postResult(List<DemoItem> items) {
        App app = (App) application;
        if (app.isVisible()) {
            //return;
        }
        Timber.v("Visible %s", app.isVisible());
/*        String title = TextUtil.getString(application, R.string.app_name);
        String message;
        if (!DataUtil.isEmpty(items)) {
            message = TextUtil.getString(app, R.string.profitable_coins, items.size());
        } else {
            message = TextUtil.getString(app, R.string.profitable_coins_motto);
        }
        notify.showNotification(application, title, message, NavigationActivity.class);*/
    }

    @DebugLog
    private void postFailed(Throwable error) {

    }
}
