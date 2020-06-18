package com.dreampany.nearby.api.util;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Hawladar Roman on 8/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class GooglePlayUtil {
    private GooglePlayUtil() {}

    public static boolean isPlayAvailable(Context context) {
       return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context.getApplicationContext()) == ConnectionResult.SUCCESS;
    }
}
