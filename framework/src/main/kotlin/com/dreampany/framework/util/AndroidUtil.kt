package com.dreampany.framework.util

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.view.Surface
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dreampany.framework.R
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.Constants
import com.google.common.base.Splitter
import com.google.common.collect.Iterables
import com.jaredrummler.android.device.DeviceName
import eu.davidea.flexibleadapter.utils.FlexibleUtils
import kotlinx.coroutines.Runnable
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by roman on 2019-07-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AndroidUtil {
    companion object {
        private var colorPrimary = -1
        private var colorPrimaryDark = -1
        private var colorAccent = -1
        private val drawable: String = "drawable"

        private var uiHandler: Handler? = null
        private val backHandler: Handler? = null

        fun hasJellyBeanMR1(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
        }

        fun hasJellyBeanMR2(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
        }

        fun hasKitkat(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        }

        fun hasLollipop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

        fun hasMarshmallow(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        fun hasNougat(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        }

        fun hasOreo(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        }

        fun hasPie(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
            if (hasMarshmallow()) {
                return Looper.getMainLooper().isCurrentThread()
            }
        }

        fun getUiHandler(): Handler {
            if (uiHandler == null) {
                uiHandler = Handler(Looper.getMainLooper())
            }
            return uiHandler!!
        }

        fun isOnUiThread(): Boolean {
            return Thread.currentThread() === Looper.getMainLooper().getThread()
        }

        fun getPackageName(context: Context?): String? {
            val packageInfo = getPackageInfo(context)
            return packageInfo?.packageName
        }

        fun getLastApplicationId(context: Context?): String? {
            val applicationId = getPackageName(context?.getApplicationContext())
            if (DataUtil.isEmpty(applicationId)) {
                return null
            }
            return Iterables.getLast(
                Splitter.on(Constants.Sep.DOT).trimResults().split(
                    applicationId!!
                )
            )
        }

        fun getVersionCode(context: Context): Int {
            val packageInfo = getPackageInfo(context)
            return packageInfo?.versionCode ?: 0
        }

        fun getVersionName(context: Context): String? {
            val packageInfo = getPackageInfo(context)
            return packageInfo?.versionName
        }

        fun getPackageInfo(context: Context?): PackageInfo? {
            return getPackageInfo(context, 0)
        }

        fun getPackageInfo(context: Context?, flags: Int): PackageInfo? {
            return getPackageInfo(context, context?.packageName, flags)
        }

        fun getPackageInfo(context: Context?, packageName: String?, flags: Int): PackageInfo? {
            try {
                if (packageName.isNullOrEmpty()) return null
                return getPackageManager(context)?.getPackageInfo(packageName, flags)
            } catch (error: PackageManager.NameNotFoundException) {
                Timber.e(error)
            }
            return null
        }

        fun getApplicationInfo(
            context: Context,
            packageName: String,
            flags: Int
        ): ApplicationInfo? {
            try {
                return getPackageManager(context)?.getApplicationInfo(packageName, flags)
            } catch (error: PackageManager.NameNotFoundException) {
                Timber.e(error)
            }
            return null
        }

        fun getPackageManager(context: Context?): PackageManager? {
            return context?.applicationContext?.packageManager
        }

        fun getInstalledApps(context: Context): List<ApplicationInfo>? {
            return getInstalledApps(context, getPackageManager(context))
        }

        fun getInstalledApps(context: Context, pm: PackageManager?): List<ApplicationInfo>? {
            return pm?.getInstalledApplications(PackageManager.GET_META_DATA)
        }

        fun isSystemApp(info: ApplicationInfo): Boolean {
            return ((info.flags and ApplicationInfo.FLAG_SYSTEM) != 0)
        }

        fun isValid(pm: PackageManager, info: ApplicationInfo): Boolean {
            return (pm.getLaunchIntentForPackage(info.packageName) != null)
        }

        fun getApplicationIcon(context: Context, packageName: String): Drawable? {
            try {
                return getPackageManager(context)?.getApplicationIcon(packageName)
            } catch (error: PackageManager.NameNotFoundException) {
                Timber.e(error)
            }
            return null
        }

        fun getApplicationIconUri(context: Context, packageName: String): Uri? {
            var result = Uri.EMPTY
            try {
                val info = getApplicationInfo(context, packageName, 0)
                if (info != null && info.icon != 0) {
                    result = Uri.parse("android.resource://" + packageName + "/" + info.icon)
                }
            } catch (error: PackageManager.NameNotFoundException) {
                Timber.e(error)
            }
            return result
        }

        fun getApplicationVersionCode(context: Context, packageName: String): Int {
            var result = 0
            try {
                val info = getPackageInfo(context, packageName, 0)
                if (info != null) {
                    result = info.versionCode
                }
            } catch (error: PackageManager.NameNotFoundException) {
                Timber.e(error)
            }
            return result
        }

        fun getApplicationVersionName(context: Context, packageName: String): String? {
            try {
                val info = getPackageInfo(context, packageName, 0)
                if (info != null) {
                    return info.versionName
                }
            } catch (error: PackageManager.NameNotFoundException) {
                Timber.e(error)
            }
            return null
        }

        fun openApplication(context: Context, packageName: String) {
            try {
                getPackageManager(context)?.getLaunchIntentForPackage(packageName)?.run {
                    context.startActivity(this)
                }
            } catch (error: Throwable) {
                Timber.e(error)
            }
        }

        fun openApplicationDetails(context: Context, packageName: String) {
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + packageName)
                context.startActivity(intent)
            } catch (error: Throwable) {
                Timber.e(error)
            }
        }

        fun isDebug(context: Context): Boolean {
            var debuggable = false

            val appContext = context.getApplicationContext()
            val pm = appContext.getPackageManager()
            try {
                val appInfo = pm.getApplicationInfo(appContext.getPackageName(), 0)
                debuggable = (0 != (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
            } catch (ignored: PackageManager.NameNotFoundException) {
                /*debuggable variable will remain false*/
            }

            return debuggable
        }

        fun isRelease(context: Context): Boolean {
            return !isDebug(context)
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun getColorPrimary(context: Context): Int {
            if (colorPrimary < 0) {
                val primaryAttr =
                    if (FlexibleUtils.hasLollipop()) android.R.attr.colorPrimary else R.attr.colorPrimary
                val androidAttr = context.getTheme().obtainStyledAttributes(intArrayOf(primaryAttr))
                colorPrimary = androidAttr.getColor(0, -0x1) //Default: material_deep_teal_500
                androidAttr.recycle()
            }
            return colorPrimary
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun getColorPrimaryDark(context: Context): Int {
            if (colorPrimaryDark < 0) {
                val primaryDarkAttr =
                    if (FlexibleUtils.hasLollipop()) android.R.attr.colorPrimaryDark else R.attr.colorPrimaryDark
                val androidAttr =
                    context.getTheme().obtainStyledAttributes(intArrayOf(primaryDarkAttr))
                colorPrimaryDark = androidAttr.getColor(0, -0x1) //Default: material_deep_teal_500
                androidAttr.recycle()
            }
            return colorPrimaryDark
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun getColorAccent(context: Context): Int {
            if (colorAccent < 0) {
                val accentAttr =
                    if (FlexibleUtils.hasLollipop()) android.R.attr.colorAccent else R.attr.colorAccent
                val androidAttr = context.getTheme().obtainStyledAttributes(intArrayOf(accentAttr))
                colorAccent = androidAttr.getColor(0, -0x1) //Default: material_deep_teal_500
                androidAttr.recycle()
            }
            return colorAccent
        }

        /**
         * Show Soft Keyboard with new Thread
         *
         * @param activity
         */
        fun hideSoftInput(activity: Activity) {
            if (activity.getCurrentFocus() != null) {
                Runnable {
                    val imm =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.hideSoftInputFromWindow(
                        activity.getCurrentFocus()!!.getWindowToken(),
                        0
                    )
                }.run()
            }
        }

        /**
         * Hide Soft Keyboard from Dialogs with new Thread
         *
         * @param context
         * @param view
         */
        fun hideSoftInputFrom(context: Context, view: View) {
            ({
                val imm =
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(view.getWindowToken(), 0)
            } as Runnable).run()
        }

        /**
         * Show Soft Keyboard with new Thread
         *
         * @param context
         * @param view
         */
        fun showSoftInput(context: Context, view: View) {
            ({
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

                imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

            } as Runnable).run()
        }

        fun getScreenOrientation(activity: Activity): Int {
            when (activity.getWindowManager().getDefaultDisplay().getRotation()) {
                Surface.ROTATION_270 -> return 270
                Surface.ROTATION_180 -> return 180
                Surface.ROTATION_90 -> return 90
                else -> return 0
            }
        }

        fun isAppInstalled(context: Context, packageName: String): Boolean {
            val pm = context.getPackageManager()
            try {
                pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                return true
            } catch (ignored: PackageManager.NameNotFoundException) {
            }

            return false
        }

        fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
            try {
                return packageManager.getApplicationInfo(packageName, 0).enabled
            } catch (ignored: PackageManager.NameNotFoundException) {
                return false
            }

        }

        fun isAlive(context: Context?): Boolean {
            if (context is Activity) {
                return isAlive(context)
            }
            return false
        }

        /**
         * To check activeness of activity
         *
         * @return true if host activity is not destroyed or finished otherwise false
         */
        fun isAlive(activity: Activity?): Boolean {
            var alive = false
            activity?.run {
                if (hasJellyBeanMR1()) {
                    alive = !(isFinishing() || isDestroyed())
                } else {
                    alive = !isFinishing()
                }
            }
            return alive
        }

        fun isAlive(fragment: Fragment?): Boolean {
            return fragment?.isVisible() ?: false
        }


        fun <T : Context> createIntent(source: T, target: Class<*>): Intent {
            return createIntent(source, target, null)
        }

        fun <T : Context> createIntent(source: T, target: Class<*>, task: Task<*>?): Intent {
            val intent = Intent(source, target)
            if (task != null) {
                intent.putExtra(Constants.Task.TASK, task as Parcelable?)
            }
            return intent
        }

        /* ui opening section */
        fun <T : Activity> openActivity(source: T, target: Class<*>) {
            openActivity(source, target, false)
        }

        fun <T : Fragment> openActivity(source: T, target: Class<*>) {
            openActivity(source, target, false)
        }

        fun <T : Activity> openActivity(source: T?, target: Class<*>, finish: Boolean) {
            source?.run {
                startActivity(Intent(source, target))
                if (finish) {
                    finish()
                }
                Animato.animateSlideLeft(this)
            }
        }

        fun <T : Fragment> openActivity(source: T?, target: Class<*>, finish: Boolean) {
            source?.run {
                startActivity(Intent(getActivity(), target))
                if (finish) {
                    getActivity()?.finish()
                }
                Animato.animateSlideLeft(getActivity())
            }
        }

        fun <T : Activity> openActivity(source: T?, target: Class<*>, requestCode: Int) {
            source?.run {
                startActivityForResult(Intent(this, target), requestCode)
                Animato.animateSlideLeft(this)
            }
        }

        fun <T : Fragment> openActivity(source: T?, target: Class<*>, requestCode: Int) {
            source?.run {
                startActivityForResult(Intent(getActivity(), target), requestCode)
                Animato.animateSlideLeft(getActivity())
            }
        }

        fun <T : Activity, X : Parcelable> openActivity(
            source: T,
            target: Class<*>,
            task: Task<X>
        ) {
            openActivity(source, target, task, false)
        }

        fun <T : Activity, X : Parcelable> openActivity(
            source: T?,
            target: Class<*>,
            task: Task<X>,
            finish: Boolean
        ) {
            source?.run {
                val intent = Intent(this, target)
                intent.putExtra(Constants.Task.TASK, task as Parcelable)
                startActivity(intent)
                if (finish) {
                    finish()
                }
                Animato.animateSlideLeft(this)
            }
        }

        fun <T : Fragment> openActivity(source: T?, target: Class<*>, task: Task<*>?) {
            source?.run {
                val intent = Intent(getActivity(), target)
                if (task != null) {
                    intent.putExtra(Constants.Task.TASK, task as Parcelable?)
                }
                startActivity(intent)
                Animato.animateSlideLeft(getActivity())
            }
        }

        fun <T : Activity, X : Parcelable> openActivity(
            source: T?,
            target: Class<*>,
            task: Task<X>,
            requestCode: Int
        ) {
            source?.run {
                val intent = Intent(this, target)
                intent.putExtra(Constants.Task.TASK, task as Parcelable)
                startActivityForResult(intent, requestCode)
                Animato.animateSlideLeft(this)
            }
        }

        fun <T : Fragment, X : Parcelable> openActivity(
            source: T?,
            target: Class<*>,
            task: Task<X>,
            requestCode: Int
        ) {
            source?.run {
                val intent = Intent(this.getActivity(), target)
                intent.putExtra(Constants.Task.TASK, task as Parcelable)
                startActivityForResult(intent, requestCode)
                Animato.animateSlideLeft(this.getActivity())
            }
        }

/*        fun  openActivity(context: Context, target: Class<*>, task: Task<*>?) {
            val intent = Intent(context, target)
            if (task != null) {
                intent.putExtra(Constants.Task.TASK, task as Parcelable?)
            }
            startActivity(intent)
            Animato.animateSlideLeft(getActivity())
        }*/

        fun sleep(time: Long): Boolean {
            try {
                Thread.sleep(time)
                return true
            } catch (ignored: InterruptedException) {
                return false
            }
        }

        fun getAndroidId(context: Context): String {
            val androidId =
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
            return androidId
        }

        fun getDeviceName(): String {
            return DeviceName.getDeviceName()
        }


        private var tts: TextToSpeech? = null

        fun initTts(context: Context) {
            if (tts == null) {
                try {
                    tts = TextToSpeech(context.getApplicationContext()) { status ->
                        if (status != TextToSpeech.ERROR) {
                            tts?.setLanguage(Locale.ENGLISH)
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    Timber.e("Error in tts: %s", e.toString())
                }

            }
        }

        fun speak(text: String?) {
            if (text.isNullOrEmpty()) return
            tts?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val params = Bundle()
                    params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1f)
                    speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                } else {
                    val params = HashMap<String, String>()
                    params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1")
                    speak(text, TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        }

        fun silentSpeak() {
            tts?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    speak("", TextToSpeech.QUEUE_FLUSH, null, null)
                } else {
                    speak("", TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        }


        fun stopTts() {
            tts?.run {
                stop()
                shutdown()
            }
            tts = null
        }

        fun backupDatabase(context: Context, databaseName: String) {
            try {
                val sd = Environment.getExternalStorageDirectory()
                val data = Environment.getDataDirectory()

                val packageName = context.getApplicationInfo().packageName

                if (sd.canWrite()) {
                    val currentDBPath = String.format(
                        "//data//%s//databases//%s",
                        packageName, databaseName
                    )
                    val backupDBPath = String.format("debug_%s.sqlite", packageName)
                    val currentDB = File(data, currentDBPath)
                    val backupDB = File(sd, backupDBPath)

                    if (currentDB.exists()) {
                        val src = FileInputStream(currentDB).getChannel()
                        val dst = FileOutputStream(backupDB).getChannel()
                        dst.transferFrom(src, 0, src.size())
                        src.close()
                        dst.close()
                    }
                }
            } catch (e: Exception) {
                throw Error(e)
            }

        }

        fun share(fragment: Fragment, subject: String, text: String) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            fragment.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        fun share(activity: Activity, subject: String, text: String) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            activity.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        fun isServiceRunning(context: Context, serviceClass: KClass<*>): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
            for (service in manager!!.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.java.name.equals(service.service.className)) {
                    return true
                }
            }
            return false
        }

        fun startService(context: Context, serviceClass: Class<*>) {
            startService(context, Intent(context, serviceClass))
        }

        fun startService(context: Context, serviceIntent: Intent) {
            ContextCompat.startForegroundService(context, serviceIntent)
        }

        @DrawableRes
        fun getDrawableRes(context: Activity, name: String): Int {
            return context.resources.getIdentifier(name, drawable, context.packageName)
        }
    }
}