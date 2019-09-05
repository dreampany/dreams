package com.dreampany.fit.service;

import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.dreampany.fit.R;
import com.dreampany.fit.misc.Constants;
import com.dreampany.fit.ui.activity.NavigationActivity;
import com.dreampany.framework.api.notify.NotifyManager;
import com.dreampany.framework.api.service.BaseService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

/**
 * Created by Roman-372 on 2/7/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class AppService extends BaseService {


    private NotifyManager notify;

    private int notifyId = Constants.Id.NotifyForeground;
    private String notifyTitle;
    private String contentText;
    private int smallIcon = R.mipmap.ic_launcher;
    private Class<?> targetClass = NavigationActivity.class;

    private String channelId = Constants.Id.NotifyForegroundChannelId;
    private String channelName;
    private String channelDescription;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notifyTitle = getString(R.string.app_name);
        contentText = getString(R.string.app_name);
        channelName = getString(R.string.app_name);
        channelDescription = getString(R.string.app_name);

        if (notify == null) {
            notify = new NotifyManager();
        }
        notify.showForegroundNotification(
                this,
                notifyId,
                notifyTitle,
                contentText,
                smallIcon,
                targetClass,
                channelId,
                channelName,
                channelDescription
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        String action = intent.getAction();
        switch (action) {
            case Constants.Action.StartFit: {
                registerStepCounter();
            }
            break;
            case Constants.Action.StopFit: {
                unregisterStepCounter();
            }
            break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterStepCounter();
        stopForeground(true);
        super.onDestroy();
    }

    private final OnDataPointListener stepListener = dataPoint -> {
        for (Field field : dataPoint.getDataType().getFields()) {
            Value val = dataPoint.getValue(field);
            Timber.v("Detected DataPoint field: %s", field.getName());
            Timber.v("Detected DataPoint value: %d", val.asInt());
        }
    };

    private void registerStepCounter() {
        Timber.v("Registering Step Counter");

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        SensorRequest request = new SensorRequest.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setSamplingRate(1, TimeUnit.MINUTES)
                .build();

        Fitness.getSensorsClient(this, account)
                .add(request, stepListener)
                .addOnCompleteListener(task -> {
                    Timber.v("Step Counter Register %s", task.isSuccessful());
                });
    }

    private void unregisterStepCounter() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Fitness.getSensorsClient(this, account)
                .remove(stepListener)
                .addOnCompleteListener(task -> {
                    Timber.v("Step Counter Unregister %s", task.isSuccessful());
                });
    }
}
