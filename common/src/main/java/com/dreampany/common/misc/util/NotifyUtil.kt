package com.dreampany.common.misc.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dreampany.common.data.model.Task

/**
 * Created by roman on 15/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NotifyUtil {
    companion object {
        fun shortToast(context: Context, @StringRes resId: Int) {
            Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show()
        }

        fun shortToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        fun createNotificationChannel(
            channelId: String,
            channelName: String,
            channelDescription: String,
            channelImportance: Int
        ): NotificationChannel? {
            if (!Util.hasOreo()) {
                return null
            }
            val channel = NotificationChannel(channelId, channelName, channelImportance)
            channel.description = channelDescription
            return channel
        }

        fun deleteNotificationChannel(
            context: Context,
            channelId: String
        ): Boolean {
            if (!Util.hasOreo()) {
                return false
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager!!.deleteNotificationChannel(channelId)
            return true
        }

        fun createNotification(
            context: Context,
            notifyId: Int,
            notifyTitle: String,
            contentText: String,
            @DrawableRes smallIcon: Int,
            targetClass: Class<*>?,
            task: Task<*, *, *>?,
            channel: NotificationChannel?,
            autoCancel: Boolean
        ): Notification? {
            val appContext = context.applicationContext
            var builder: NotificationCompat.Builder? = null
            if (Util.hasOreo()) {
                val manager = NotificationManagerCompat.from(appContext)
                channel?.run {
                    manager.createNotificationChannel(this)
                    builder = NotificationCompat.Builder(context, this.id)
                }
            } else {
                builder = NotificationCompat.Builder(context)
            }

            val showTaskIntent = Util.createIntent(appContext, targetClass!!, task)
            showTaskIntent.action = Intent.ACTION_MAIN
            //showTaskIntent.setAction(Long.toString(System.currentTimeMillis()));
            showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            val contentIntent = PendingIntent.getActivity(
                appContext,
                notifyId,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            return builder?.setContentTitle(notifyTitle)?.
            setContentText(contentText)?.
            setSmallIcon(smallIcon)?.
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))?.
            setWhen(System.currentTimeMillis())?.
            setContentIntent(contentIntent)?.
            setAutoCancel(autoCancel)?.
            build()
        }
    }
}