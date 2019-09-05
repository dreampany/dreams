package com.dreampany.cast.ui.fragment;

import android.os.Bundle;

import com.dreampany.cast.R;
import com.dreampany.cast.data.source.pref.Pref;
import com.dreampany.cast.service.NotifyService;
import com.dreampany.framework.api.service.ServiceManager;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

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
    ServiceManager service;
    Disposable disposable;

    @Inject
    public SettingsFragment() {

    }

    @Override
    public int getPrefLayoutId() {
        return R.xml.settings;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        ex.postToUi(this::initView);
    }

    @Override
    protected void onStopUi() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.isDisposed();
        }
    }

    private void initView() {
        setTitle(R.string.settings);
        String notifyKey = getString(R.string.key_notification);
        Flowable<Boolean> flowable = pref.observePublicly(notifyKey, Boolean.class, true);
        Disposable disposable = rx
                .backToMain(flowable)
                .subscribe(enabled -> {
                    Timber.v("Notification %s" , enabled);
                    if (enabled) {
                        service.schedulePowerService(NotifyService.class, 10);
                    } else {
                        service.cancel(NotifyService.class);
                    }
                });
        this.disposable = disposable;
    }
}
