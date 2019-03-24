package com.dreampany.word.vm;

import android.app.Application;

import com.dreampany.frame.api.notify.NotifyManager;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.word.R;
import com.dreampany.word.app.App;
import com.dreampany.word.ui.activity.NavigationActivity;
import com.dreampany.word.ui.model.WordItem;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
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
    @Inject NotifyManager notify;
    private Disposable disposable;

    @Inject
    NotifyViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    NetworkManager network) {
        this.application = application;
        this.rx = rx;
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
        this.disposable = rx.backToMain(getNextItem())
                .subscribe(this::postResult, this::postFailed);
    }

    private Maybe<WordItem> getNextItem() {
        return Maybe.empty();
    }


    private boolean hasDisposable() {
        return disposable != null && !disposable.isDisposed();
    }

    @DebugLog
    private void postResult(WordItem item) {
        App app = (App) application;
        if (app.isVisible()) {
            //return;
        }
        Timber.v("Visible %s", app.isVisible());
        String title = TextUtil.getString(application, R.string.app_name);
        String message = TextUtil.getString(application, R.string.learn_word);
 /*       if (!DataUtil.isEmpty(items)) {
            message = TextUtil.getString(app, R.string.profitable_coins, items.size());
        } else {
            message = TextUtil.getString(app, R.string.profitable_coins_motto);
        }*/
        //notify.showNotification(application, title, message, NavigationActivity.class);
    }

    @DebugLog
    private void postFailed(Throwable error) {

    }
}
