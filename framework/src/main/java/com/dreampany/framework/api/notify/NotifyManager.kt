package com.dreampany.framework.api.notify

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import com.dreampany.framework.R
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.isMinO
import com.dreampany.framework.misc.util.NotifyUtil
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NotifyManager
@Inject constructor(val context: Context) {
    private val icon: Bitmap? = null

    private var manager: NotificationManagerCompat? = null

    @Synchronized
    fun showNotification(
        title: String,
        contentText: String,
        icon: Int,
        targetClass: Class<*>?,
        task: Task<*, *, *, *, *>?
    ) {
        showNotification(
            Constants.Notify.DEFAULT_ID,
            title,
            contentText,
            icon,
            targetClass,
            task,
            Constants.Notify.DEFAULT_CHANNEL_ID,
            context.getString(R.string.app_name),
            context.getString(R.string.description)
        )
    }

    fun showNotification(
        contentText: String,
        icon: Int,
        channelId: String,
        targetClass: Class<*>?,
        task: Task<*, *, *, *, *>?
    ) {
        showNotification(
            Constants.Notify.DEFAULT_ID,
            context.getString(R.string.app_name),
            contentText,
            icon,
            targetClass,
            task,
            channelId,
            context.getString(R.string.app_name),
            context.getString(R.string.description)
        )
    }

    @Synchronized
    fun showNotification(
        title: String,
        contentText: String,
        icon: Int,
        notifyId: Int,
        channelId: String,
        targetClass: Class<*>?,
        task: Task<*, *, *, *, *>?
    ) {
        showNotification(
            notifyId,
            title,
            contentText,
            icon,
            targetClass,
            task,
            channelId,
            context.getString(R.string.app_name),
            context.getString(R.string.description)
        )
    }

    @SuppressLint("NewApi")
    @Synchronized
    fun showNotification(
        notifyId: Int,
        notifyTitle: String,
        contentText: String,
        smallIcon: Int,
        targetClass: Class<*>?,
        task: Task<*, *, *, *, *>?,
        channelId: String,
        channelName: String,
        channelDescription: String
    ) {
        val appContext = context.applicationContext

        if (manager == null) {
            manager = NotificationManagerCompat.from(appContext)
        }

        val channel = NotifyUtil.createNotificationChannel(
            channelId,
            channelName,
            channelDescription,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )

        val notification = NotifyUtil.createNotification(
            context,
            notifyId,
            notifyTitle,
            contentText,
            smallIcon,
            targetClass,
            task,
            channel,
            true
        )

        notification?.run {
            manager?.notify(notifyId, this)
        }
    }

    @SuppressLint("NewApi")
    fun showForegroundNotification(
        context: Context,
        notifyId: Int,
        notifyTitle: String,
        contentText: String,
        smallIcon: Int,
        targetClass: Class<*>,
        task: Task<*, *, *, *, *>?,
        channelId: String,
        channelName: String,
        channelDescription: String
    ) {
        val appContext = context.applicationContext

        if (manager == null) {
            manager = NotificationManagerCompat.from(appContext)
        }

        val channel = NotifyUtil.createNotificationChannel(
            channelId,
            channelName,
            channelDescription,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )

        val notification = NotifyUtil.createNotification(
            context,
            notifyId,
            notifyTitle,
            contentText,
            smallIcon,
            targetClass,
            task,
            channel,
            true
        )

        notification?.run {
            if (isMinO) {
                (context as Service).startForeground(notifyId, this)
            }
            else {
                manager?.notify(notifyId, this)
            }
        }
    }

    fun showNotification(
        context: Context,
        notifyId: Int,
        title: String,
        message: String,
        target: Class<*>,
        data: Bundle?
    ) {

        //Notify.cancelNotification(notifyId)
        //Notify.with(context).content { }.show()
    }
}