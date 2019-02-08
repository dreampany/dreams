package com.dreampany.fit.service;

import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.dreampany.fit.R;
import com.dreampany.fit.misc.Constants;
import com.dreampany.fit.ui.activity.NavigationActivity;
import com.dreampany.frame.api.notify.NotifyManager;
import com.dreampany.frame.api.service.BaseService;

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
        if (intent != null) {

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}
