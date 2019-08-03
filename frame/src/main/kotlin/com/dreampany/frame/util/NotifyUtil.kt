package com.dreampany.frame.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dreampany.frame.R
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.misc.AppExecutors
import com.muddzdev.styleabletoast.StyleableToast
import com.onurkaganaldemir.ktoastlib.KToast

/**
 * Created by roman on 2019-07-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NotifyUtil {
    companion object {
        fun longToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }

        fun shortToast(context: Context, @StringRes resId: Int) {
            Toast.makeText(context, TextUtil.getString(context, resId), Toast.LENGTH_SHORT).show()
        }

        fun shortToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        fun toast(context: Context, ex: AppExecutors, text: String) {
            toast(context, ex, text, Toast.LENGTH_SHORT)
        }

        fun toast(context: Context, ex: AppExecutors, text: String, duration: Int) {
            val toast = Toast.makeText(context, text, duration)
            if (AndroidUtil.isOnUiThread()) {
                toast.show()
            } else {
                ex.postToUi { toast.show() }
            }
        }

        fun showInfo(activity: Activity, info: String) {
            KToast.successToast(activity, info, Gravity.BOTTOM, KToast.LENGTH_AUTO);
        }

        fun showInfo(context: Context, @StringRes resId: Int) {
            showInfo(context, context.getString(resId))
        }

        fun showInfo(context: Context, info: String) {
            StyleableToast.Builder(context)
                .text(info)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(context, R.color.material_green700))
                .length(Toast.LENGTH_SHORT)
                .show()
        }

        fun showError(context: Context, error: String) {
            StyleableToast.Builder(context)
                .text(error)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(context, R.color.material_red700))
                .length(Toast.LENGTH_SHORT)
                .show()
        }

        fun showProgress(context: Context, error: String) {
            StyleableToast.Builder(context)
                .text(error)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(context, R.color.material_red700))
                .length(Toast.LENGTH_SHORT)
                .show()
        }


        fun createNotificationChannel(
            channelId: String,
            channelName: String,
            channelDescription: String,
            channelImportance: Int
        ): NotificationChannel? {
            if (!AndroidUtil.hasOreo()) {
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
            if (!AndroidUtil.hasOreo()) {
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
            task: Task<*>?,
            channel: NotificationChannel?,
            autoCancel: Boolean
        ): Notification? {
            val appContext = context.applicationContext
            var builder: NotificationCompat.Builder? = null
            if (AndroidUtil.hasOreo()) {
                val manager = NotificationManagerCompat.from(appContext)
                channel?.run {
                    manager.createNotificationChannel(this)
                    builder = NotificationCompat.Builder(context, this.id)
                }
            } else {
                builder = NotificationCompat.Builder(context)
            }

            val showTaskIntent = AndroidUtil.createIntent(appContext, targetClass!!, task)
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