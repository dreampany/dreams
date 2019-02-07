package com.dreampany.frame.util;


import android.content.Context;
import android.widget.Toast;

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


    public static String createNotificationChannel(Context context) {
        if (!AndroidUtil.hasOreo()) {
            return null;
        }

        return null;
    }
}
