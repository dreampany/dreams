package com.dreampany.framework.util;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class BarUtil {

    private BarUtil() {}

    public static void hide(AppCompatActivity activity) {
        if (AndroidUtil.Companion.hasJellyBeanMR2()) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

/*            if (AndroidUtil.hasKitkat()) {
                uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE;
            }*/
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar bar = activity.getSupportActionBar();
            if (bar != null) {
                bar.hide();
            }
        }
    }

    public static void show(AppCompatActivity activity) {
        if (activity != null && AndroidUtil.Companion.hasJellyBeanMR2()) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;

            decorView.setSystemUiVisibility(uiOptions);
            ActionBar bar = activity.getSupportActionBar();
            if (bar != null) {
                bar.show();
            }
        }
    }

    public static void show(Toolbar toolbar) {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    public static void hide(Toolbar toolbar) {
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();    }
}
