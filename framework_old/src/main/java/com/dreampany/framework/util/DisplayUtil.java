package com.dreampany.framework.util;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import androidx.annotation.DimenRes;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class DisplayUtil {
    private DisplayUtil() {}

    public static float dp2px(final Context context, final float dpValue) {
        return dpValue * context.getResources().getDisplayMetrics().density;
    }

    public static boolean isScreenOn(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    screenOn = true;
                }
            }
            return screenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //noinspection deprecation
            return pm.isScreenOn();
        }
    }

    public static int getScreenWidthInDp(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return Math.round(dm.widthPixels / dm.density);
    }

    public static int getScreenWidthInPx(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * Resolves a dimension resource that uses scaled pixels
     *
     * @param context the current context
     * @param sizeRes the dimension resource holding an SP value
     * @return the text size in pixels
     */
    public static float pixelsFromSpResource(Context context, @DimenRes int sizeRes) {
        final Resources res = context.getResources();
        return res.getDimension(sizeRes) / res.getDisplayMetrics().density;
    }

    /**
     * Resolves a dimension resource that uses density-independent pixels
     *
     * @param context the current context
     * @param res     the dimension resource holding a DP value
     * @return the size in pixels
     */
    public static float pixelsFromDpResource(Context context, @DimenRes int res) {
        return context.getResources().getDimension(res);
    }

    /**
     * Converts density-independent pixels to pixels
     * @param dip the dips
     * @return size in pixels
     */
    public static int dpToPixels(float dip) {
        return (int) (dip * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Converts pixels to density-independent pixels
     * @param pixels the pixels
     * @return size in dp
     */
    public static int pixelsToDp(float pixels) {
        return (int) (pixels / Resources.getSystem().getDisplayMetrics().density);
    }
}
