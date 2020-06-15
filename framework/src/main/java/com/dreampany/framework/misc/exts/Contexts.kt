package com.dreampany.framework.misc.exts

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.dreampany.framework.R
import com.dreampany.framework.misc.constant.Constants
import com.google.common.base.Splitter
import com.google.common.collect.Iterables
import timber.log.Timber
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
val Context?.appContext: Context?
    get() = this?.applicationContext

val Context?.screenWidth: Int
    get() {
        val dm = DisplayMetrics()
        val wm = appContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager? ?: return 0
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

val Context?.screenHeight: Int
    get() {
        val dm = DisplayMetrics()
        val wm = appContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager? ?: return 0
        wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

val Context?.isNull: Boolean
    get() = this == null

val Context?.isNotNull: Boolean
    get() = this != null

/*fun Context?.isDebug(): Boolean {
    if (this == null) return true
    var debug = true
    try {
        val appInfo = this.applicationContext.packageManager.getApplicationInfo(
            this.applicationContext.getPackageName(), 0
        )
        debug = (0 != (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
    } catch (ignored: PackageManager.NameNotFoundException) {
    }
    return debug
}*/

val Context?.mediaDir: File?
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() {
        val context: Context = appContext ?: return null
        var mediaDir: File? = null
        if (isMinLollipop) {
            mediaDir = context.externalMediaDirs?.firstOrNull()
                ?.let { File(it, context.getString(R.string.app_name)).apply { mkdirs() } }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

val Context?.isDebug: Boolean
    get() {
        if (this == null) return true
        var debug = true
        try {
            val appInfo = this.applicationContext.packageManager.getApplicationInfo(
                this.applicationContext.getPackageName(), 0
            )
            debug = (0 != (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        return debug
    }

fun Context?.dimension(@DimenRes resId: Int): Float = this?.resources?.getDimension(resId) ?: 0.0f

val Context?.density: Float
    get() = this?.resources?.displayMetrics?.density ?: 0.0f

fun Context?.dpToPx(dp: Float): Int = (dp * density).toInt()

@ColorInt
fun Context?.color(@ColorRes resId: Int): Int =
    if (this == null) 0 else ContextCompat.getColor(this, resId)

/*@ColorInt
fun Int.toColor(context: Context): Int = ContextCompat.getColor(context, this)*/

fun Context?.currentTask(): ActivityManager.RunningTaskInfo? {
    val manager = this?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    return manager?.getRunningTasks(1)?.first()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Context?.currentUsageStats(): UsageStats? {
    val manager = this?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?
    val endMillis = System.currentTimeMillis()
    val startMillis = endMillis - TimeUnit.MINUTES.toMillis(1)
    val stats = manager?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startMillis, endMillis)
    val sortedMap = TreeMap<Long, UsageStats>()
    stats?.forEach {
        sortedMap.put(it.lastTimeUsed, it)
    }
    return sortedMap.lastEntry()?.value
}

fun Context?.currentPackage(): String? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        return currentUsageStats()?.packageName
    }
    return currentTask()?.topActivity?.packageName
}

val Context?.packageManager: PackageManager? get() = this?.applicationContext?.packageManager

val Context?.activityManager: ActivityManager?
    get() = this?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?


val Context?.packageName: String? get() = appContext?.packageName

fun Context?.packageInfo(pkg: String?, flags: Int): PackageInfo? {
    try {
        if (pkg.isNullOrEmpty()) return null
        return packageManager?.getPackageInfo(pkg, flags)
    } catch (error: PackageManager.NameNotFoundException) {
        //Timber.e(error)
    }
    return null
}

fun Context?.icon(pkg: String?): Drawable? {
    if (pkg.isNullOrEmpty()) return null
    try {
        return packageManager?.getApplicationIcon(pkg)
    } catch (error: PackageManager.NameNotFoundException) {
        //Timber.e(error)
    }
    return null
}

@SuppressLint("MissingPermission")
fun Context?.kill(pkg: String?) {
    if (pkg.isNullOrEmpty()) return
    activityManager?.killBackgroundProcesses(pkg)
}

fun Context?.lastApplicationId(): String? {
    val applicationId = this.packageName ?: return Constants.Default.NULL

    return Iterables.getLast(Splitter.on(Constants.Sep.DOT).trimResults().split(applicationId))
}

fun Context.formatString(@StringRes formatRes: Int, vararg values: Any): String =
    String.format(getString(formatRes), *values)

val Context?.countryCode: String
    get() {
        if (this == null) return Locale.ENGLISH.country
        var code: String? = null
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        if (tm != null) {
            code = tm.simCountryIso
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
                code = resources.configuration.getLocales().get(0).getCountry();
            } else {
                code = resources.configuration.locale.getCountry();
            }
        }
        if (!code.isNullOrEmpty() && code.length == 2) return code.toUpperCase()
        return Locale.ENGLISH.country
    }

val Context?.country : String
    get() {
        val code = countryCode
        val locale = Locale("", code)
        return locale.displayCountry
    }

@SuppressLint("PrivateApi")
fun getCDMACountryIso(): String? {
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

val Context?.locationManager: LocationManager?
    get() = this?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?


