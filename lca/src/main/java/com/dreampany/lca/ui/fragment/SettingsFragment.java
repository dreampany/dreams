package com.dreampany.lca.ui.fragment;

import android.os.Bundle;

import com.dreampany.frame.api.service.JobManager;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.lca.R;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.misc.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Hawladar Roman on 5/28/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class SettingsFragment extends BaseMenuFragment {

    @Inject
    Pref pref;
    @Inject
    RxMapper rx;
    @Inject
    AppExecutors ex;
    @Inject
    JobManager job;
    private CompositeDisposable disposables;

    @Inject
    public SettingsFragment() {
        disposables = new CompositeDisposable();
    }

    @Override
    public int getPrefLayoutId() {
        return R.xml.settings;
    }

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Companion.settings(Objects.requireNonNull(getAppContext()));
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        ex.postToUi(this::initView);
    }

    @Override
    protected void onStopUi() {
        disposables.clear();
    }

    private void initView() {
        setTitle(R.string.settings);
        String notifyCoin = getString(R.string.key_notify_coin);
        String notifyNews = getString(R.string.key_notify_news);
        Flowable<Boolean> coinFlowable = pref.observePublicly(notifyCoin, Boolean.class, true);
        disposables.add(rx
                .backToMain(coinFlowable)
                .subscribe(enabled -> {
                    adjustNotify();
                }));
        Flowable<Boolean>  newsFlowable = pref.observePublicly(notifyNews, Boolean.class, false);
        disposables.add(rx
                .backToMain(newsFlowable)
                .subscribe(enabled -> {
                    adjustNotify();
                }));
    }

    private final Runnable runner = this::configJob;

    private void configJob() {
        if (pref.hasNotification()) {
            job.create(
                    Constants.Tag.NOTIFY_SERVICE,
                    NotifyService.class,
                    (int) Constants.Delay.INSTANCE.getNotify(),
                    (int) Constants.Period.INSTANCE.getNotify()
            );
        } else {
            job.cancel(Constants.Tag.NOTIFY_SERVICE);
        }
    }

    private void adjustNotify() {
        ex.postToUi(runner, 2000);
    }
}
