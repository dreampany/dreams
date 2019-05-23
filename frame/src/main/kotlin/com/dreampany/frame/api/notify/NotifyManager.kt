package com.dreampany.frame.api.notify

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import com.dreampany.frame.R
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.NotifyUtil
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/23/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
class NotifyManager @Inject constructor(val context: Context) {
    private val NOTIFY_FOREGROUND_DEFAULT = 101
    private val NOTIFY_DEFAULT = 102
    private val NOTIFY_CHANNEL_DEFAULT = "103"
    private val NOTIFY_IDENTIFIER = "102"

    private val icon: Bitmap? = null

    private var manager: NotificationManagerCompat? = null

/*    init {
        Notify.defaultConfig {
            header {
                color = AndroidUtil.getColorPrimaryDark(context)
            }
            alerting(Notify.CHANNEL_DEFAULT_KEY) {
                lightColor = Color.RED
            }
        }
    }*/

    fun showNotification(
        title: String,
        contentText: String,
        icon: Int,
        targetClass: Class<*>?,
        task: Task<*>?
    ) {
        showNotification(
            NOTIFY_DEFAULT,
            title,
            contentText,
            icon,
            targetClass,
            task,
            NOTIFY_CHANNEL_DEFAULT,
            context.getString(R.string.app_name),
            context.getString(R.string.description)
        )
    }

    fun showNotification(
        contentText: String,
        icon: Int,
        channelId: String,
        targetClass: Class<*>?,
        task: Task<*>?
    ) {
        showNotification(
            NOTIFY_DEFAULT,
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

    fun showNotification(
        title : String,
        contentText: String,
        icon: Int,
        notifyId: Int,
        channelId: String,
        targetClass: Class<*>?,
        task: Task<*>?
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

    fun showNotification(
        notifyId: Int,
        notifyTitle: String,
        contentText: String,
        smallIcon: Int,
        targetClass: Class<*>?,
        task: Task<*>?,
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
            notifyTitle,
            contentText,
            smallIcon,
            targetClass,
            task,
            channel,
            true
        )

        manager?.notify(notifyId, notification)
    }

    fun showForegroundNotification(
        context: Context,
        notifyId: Int,
        notifyTitle: String,
        contentText: String,
        smallIcon: Int,
        targetClass: Class<*>,
        task: Task<*>?,
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
            notifyTitle,
            contentText,
            smallIcon,
            targetClass,
            task,
            channel,
            true
        )

        if (AndroidUtil.hasOreo()) {
            (context as Service).startForeground(notifyId, notification)
        } else {
            manager?.notify(notifyId, notification)
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