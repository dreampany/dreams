package com.dreampany.framework.util;

import android.content.Context;

import com.dreampany.framework.R;
import com.dreampany.framework.misc.exceptions.DeviceUnsupportedException;
import com.dreampany.framework.misc.exceptions.GooglePlayServicesNotInstalledException;
import com.dreampany.framework.misc.exceptions.GooglePlayServicesOutDatedException;
import com.dreampany.framework.misc.exceptions.UnknownErrorException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Hawladar Roman on 6/11/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class GoogleUtil {

    public static void verifyGooglePlayService(Context context) throws DeviceUnsupportedException, GooglePlayServicesOutDatedException, GooglePlayServicesNotInstalledException, UnknownErrorException {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int status = googleAPI.isGooglePlayServicesAvailable(context);

        if (status == ConnectionResult.SUCCESS) {
            return;
        }

        if (googleAPI.isUserResolvableError(status)) {
            switch (status) {
                case ConnectionResult.SERVICE_MISSING: {
                    throw new GooglePlayServicesNotInstalledException(context.getString(R.string.google_play_service_not_installed));
                }
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED: {
                    throw new GooglePlayServicesOutDatedException(context.getString(R.string.google_play_service_out_date));
                }
                default: {
                    throw new UnknownErrorException(context.getString(R.string.google_play_service_unknown_error, status));
                }
            }
        } else {
            throw new DeviceUnsupportedException(context.getString(R.string.google_play_service_unsupported_device));
        }
    }
}
