package com.dreampany.framework.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager
import com.dreampany.framework.misc.Constants
import timber.log.Timber


/**
 * Created by roman on 2019-10-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class GeoUtil {
    companion object {

        fun getCountryCode(context: Context): String? {
            var code: String? = null
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
            if (tm != null) {
                var code = tm.simCountryIso
                if (code.isNullOrEmpty()) {
                    if (tm.phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                        code = getCDMACountryIso()
                    } else {
                        code = tm.getNetworkCountryIso()
                    }
                }
            }
            if (code.isNullOrEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    code =
                        context.getResources().getConfiguration().getLocales().get(0).getCountry();
                } else {
                    code = context.getResources().getConfiguration().locale.getCountry();
                }
            }
            if (!code.isNullOrEmpty() && code.length == 2) return code
            return null
        }

        @SuppressLint("PrivateApi")
        private fun getCDMACountryIso(): String? {
            try {
                // try to get country code from SystemProperties private class
                val systemProperties = Class.forName("android.os.SystemProperties")
                val get = systemProperties.getMethod("get", String::class.java)
                // get homeOperator that contain MCC + MNC
                val homeOperator =
                    get.invoke(systemProperties, "ro.cdma.home.operator.numeric") as String
                // first 3 chars (MCC) from homeOperator represents the country code
                val mcc = Integer.parseInt(homeOperator.substring(0, 3))
                // mapping just countries that actually use CDMA networks
                when (mcc) {
                    330 -> return "PR"
                    310 -> return "US"
                    311 -> return "US"
                    312 -> return "US"
                    316 -> return "US"
                    283 -> return "AM"
                    460 -> return "CN"
                    455 -> return "MO"
                    414 -> return "MM"
                    619 -> return "SL"
                    450 -> return "KR"
                    634 -> return "SD"
                    434 -> return "UZ"
                    232 -> return "AT"
                    204 -> return "NL"
                    262 -> return "DE"
                    247 -> return "LV"
                    255 -> return "UA"
                }
            } catch (error: Throwable) {
                Timber.e(error)
            }
            return null
        }
    }
}