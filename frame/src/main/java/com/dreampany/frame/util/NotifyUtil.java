package com.dreampany.frame.util;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;
import com.dreampany.frame.R;
import com.dreampany.frame.misc.AppExecutors;
import com.muddzdev.styleabletoast.StyleableToast;

/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class NotifyUtil {
    private NotifyUtil() {
    }

    public static void longToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, AppExecutors ex, final String text) {
        toast(context, ex, text, Toast.LENGTH_SHORT);
    }

    public static void toast(final Context context, AppExecutors ex, final String text, final int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        if (AndroidUtil.isOnUiThread()) {
            toast.show();
        } else {
            ex.postToUi(toast::show);
        }
    }

    public static void showInfo(Context context, String info) {
        new StyleableToast
                .Builder(context)
                .text(info)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(context, R.color.material_green700))
                .length(Toast.LENGTH_SHORT)
                .show();
    }

    public static void showError(Context context, String error) {
        new StyleableToast
                .Builder(context)
                .text(error)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(context, R.color.material_red700))
                .length(Toast.LENGTH_SHORT)
                .show();
    }

    public static void showProgress(Context context, String error) {
        new StyleableToast
                .Builder(context)
                .text(error)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(context, R.color.material_red700))
                .length(Toast.LENGTH_SHORT)
                .show();
    }


    public static NotificationChannel createNotificationChannel(//Context context,
                                                   String channelId,
                                                   String channelName,
                                                   String channelDescription,
                                                   int channelImportance) {
        if (!AndroidUtil.hasOreo()) {
            return null;
        }
        //Context appContext = context.getApplicationContext();
        NotificationChannel channel = new NotificationChannel(channelId, channelName, channelImportance);
        channel.setDescription(channelDescription);

/*        NotificationManager manager = appContext.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);*/
        return channel;
    }

    public static boolean deleteNotificationChannel(Context context,
                                                    String channelId) {
        if (!AndroidUtil.hasOreo()) {
            return false;
        }
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.deleteNotificationChannel(channelId);
        return true;
    }

    public static Notification createNotification(Context context,
                                                  String notifyTitle,
                                                  String contentText,
                                                  @DrawableRes int smallIcon,
                                                  Class<?> targetClass,
                                                  NotificationChannel channel) {
        Context appContext = context.getApplicationContext();
        NotificationCompat.Builder builder;
        if (AndroidUtil.hasOreo()) {
            NotificationManager manager = appContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, channel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Intent showTaskIntent = new Intent(appContext, targetClass);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                appContext,
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return builder.setContentTitle(notifyTitle)
                //.setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();
    }
}
