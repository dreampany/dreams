package com.dreampany.common.misc.extension

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.dreampany.common.misc.constant.Constants
import com.google.common.base.Splitter
import com.google.common.collect.Iterables
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun Context?.appContext(): Context? {
    return this?.applicationContext
}

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

fun Context?.isNull(): Boolean {
    return this == null
}

fun Context?.isNotNull(): Boolean {
    return this != null
}

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

fun Context?.color(@ColorRes resId: Int): Int =
    if (this == null) 0 else ContextCompat.getColor(this, resId)

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

fun Context?.packageManager(): PackageManager? = this?.applicationContext?.packageManager

fun Context?.activityManager(): ActivityManager? {
    return this?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
}

fun Context?.packageName(): String? = appContext()?.packageName

fun Context?.packageInfo(pkg: String?, flags: Int): PackageInfo? {
    try {
        if (pkg.isNullOrEmpty()) return null
        return packageManager()?.getPackageInfo(pkg, flags)
    } catch (error: PackageManager.NameNotFoundException) {
        //Timber.e(error)
    }
    return null
}

fun Context?.icon(pkg: String?): Drawable? {
    if (pkg.isNullOrEmpty()) return null
    try {
        return packageManager()?.getApplicationIcon(pkg)
    } catch (error: PackageManager.NameNotFoundException) {
        //Timber.e(error)
    }
    return null
}

@SuppressLint("MissingPermission")
fun Context?.kill(pkg: String?) {
    if (pkg.isNullOrEmpty()) return
    activityManager()?.killBackgroundProcesses(pkg)
}

fun Context?.lastApplicationId(): String? {
    val applicationId = this.packageName() ?: return Constants.Default.NULL

    return Iterables.getLast(Splitter.on(Constants.Sep.DOT).trimResults().split(applicationId))
}

fun Context.formatString(@StringRes formatRes: Int, vararg values: Any): String =
    String.format(getString(formatRes), *values)


