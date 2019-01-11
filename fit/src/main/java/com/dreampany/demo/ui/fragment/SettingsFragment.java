package com.dreampany.demo.ui.fragment;

import android.os.Bundle;

import com.dreampany.demo.R;
import com.dreampany.demo.data.source.room.Pref;
import com.dreampany.demo.service.NotifyService;
import com.dreampany.frame.api.service.ServiceManager;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;

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
        Flowable<Boolean> flowable = pref.observePublic(notifyKey, Boolean.class, true);
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
