package com.dreampany.frame.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import com.dreampany.frame.R;
import com.dreampany.frame.misc.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.webianks.easy_feedback.EasyFeedback;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class SettingsUtil {

    private SettingsUtil() {
    }

    public static void moreApps(Activity activity) {
        moreApps(activity, null);
    }

    public static void moreApps(Activity activity, FirebaseAnalytics analytics) {
        try {
            Uri uri = Uri.parse("market://search?q=pub:" + activity.getString(R.string.id_google_play));
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException error) {
            Timber.e(error);
            if (analytics != null) {
                Bundle params = new Bundle();
                params.putString(FirebaseAnalytics.Param.CONTENT, error.getMessage());
                analytics.logEvent(Constants.Tag.MORE_APPS, params);
            }
        }
    }

    public static void rateUs(Activity activity) {
        rateUs(activity, null);
    }

    public static void rateUs(Activity activity, FirebaseAnalytics analytics) {
        try {
            String id = AndroidUtil.getPackageName(activity);
            Uri uri = Uri.parse("market://details?id=" + id);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException error) {
            Timber.e(error);
            if (analytics != null) {
                Bundle params = new Bundle();
                params.putString(FirebaseAnalytics.Param.CONTENT, error.getMessage());
                analytics.logEvent(Constants.Tag.RATE_US, params);
            }
        }
    }

    public static void feedback(Activity activity) {
        EasyFeedback.Builder feedback = new EasyFeedback.Builder(activity)
                .withEmail(activity.getString(R.string.email));
        if (!AndroidUtil.hasNougat()) {
            feedback.withSystemInfo();
        }
        feedback.build().start();
    }
}
