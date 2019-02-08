package com.dreampany.frame.api.notify

import android.app.Notification
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import br.com.goncalves.pugnotification.interfaces.ImageLoader
import br.com.goncalves.pugnotification.notification.PugNotification
import com.dreampany.frame.R
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
class NotifyManager @Inject constructor() {
    private val NOTIFY_FOREGROUND_DEFAULT = 101
    private val NOTIFY_DEFAULT = 102
    private val NOTIFY_IDENTIFIER = "102"

    private val icon: Bitmap? = null

    private var manager: NotificationManagerCompat? = null

    fun showForegroundNotification(
        context: Context,
        notifyId: Int,
        notifyTitle: String,
        contentText: String,
        smallIcon : Int,
        targetClass: Class<*>,
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
            channel
        )

        if (AndroidUtil.hasOreo()) {
            (context as Service).startForeground(notifyId, notification)
        } else {
            manager?.notify(notifyId, notification);
        }
    }

    fun showNotification(context: Context, title: String, message: String, target: Class<*>) {
        showNotification(context, title, message, target, null)
    }

    fun showNotification(context: Context, title: String, message: String, target: Class<*>, data: Bundle?) {
        PugNotification.with(context).cancel(NOTIFY_DEFAULT)
        PugNotification.with(context)
            .load()
            .identifier(NOTIFY_DEFAULT)
            .title(title)
            .message(message)
            .smallIcon(R.mipmap.ic_launcher)
            .largeIcon(R.mipmap.ic_launcher)
            .flags(Notification.DEFAULT_ALL)
            .autoCancel(true)
            .click(target, data)
            .simple()
            .build()
    }

    fun showNotification(
        context: Context,
        icon: Int,
        iconUri: String,
        title: String,
        message: String,
        bigText: String,
        target: Class<*>,
        data: Bundle,
        loader: ImageLoader
    ) {
        PugNotification.with(context)
            .load()
            .identifier(NOTIFY_DEFAULT)
            .title(title)
            .message(message)
            .bigTextStyle(bigText)
            .smallIcon(icon)
            .largeIcon(icon)
            .flags(Notification.DEFAULT_ALL)
            .autoCancel(true)
            .click(target, data)
            .custom()
            .setPlaceholder(icon)
            .setImageLoader(loader)
            .background(iconUri)
            .build()
    }

/*    fun postAlert(id: String, title: String) {
        postAlert(id, title, null, null)
    }

    fun postAlert(id: String, title: String, comment: String?, cause: NotifyCause?) {
        val event = NotifyEvent(id)
        event.setUi(Ui.ALERT)
        event.setTitle(title)
        event.setComment(comment)
        event.setCause(cause)
        post(event)
    }

    fun postProgress(id: String, title: String, comment: String) {
        val event = NotifyEvent(id)
        event.setUi(Ui.PROGRESS)
        event.setTitle(title)
        event.setComment(comment)
        post(event)
    }

    fun postState(id: String, title: String, comment: String, cause: Type) {
        postState(id, title, comment, null, null, cause)
    }

    fun postState(id: String, title: String, comment: String, type: Type?, subtype: Type?, cause: Type) {
        val event = NotifyEvent(id)
        event.setTitle(title)
        event.setComment(comment)
        event.setType(type)
        event.setSubtype(subtype)
        event.setCause(cause)
        event.setUi(Ui.STATE)
        post(event)
    }*/
}