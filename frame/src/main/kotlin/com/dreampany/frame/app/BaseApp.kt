package com.dreampany.frame.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import com.beardedhen.androidbootstrap.TypefaceProvider
import com.dreampany.frame.BuildConfig
import com.dreampany.frame.R
import com.dreampany.frame.api.service.JobManager
import com.dreampany.frame.api.service.ServiceManager
import com.dreampany.frame.data.model.Color
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.misc.SmartAd
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.TextUtil
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.internal.Supplier
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.stetho.Stetho
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.Indexable
import com.google.firebase.appindexing.builders.Indexables
import com.squareup.leakcanary.LeakCanary
import dagger.android.support.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import org.apache.commons.lang3.exception.ExceptionUtils
import timber.log.Timber
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
abstract class BaseApp : DaggerApplication(), Application.ActivityLifecycleCallbacks {

    //protected var context: CondomContext? = null
    @Inject
    internal lateinit var ex: AppExecutors
    protected var refs: WeakReference<Activity>? = null
    var action: Action? = null
    var indexable: Indexable? = null

    private lateinit var color: Color

    @Inject
    lateinit var ad: SmartAd
    @Inject
    lateinit var service: ServiceManager
    @Inject
    lateinit var job: JobManager


    @Volatile
    internal var visible: Boolean = false

    open fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    open fun hasStrict(): Boolean {
        return false
    }

    open fun hasLeakCanary(): Boolean {
        return false
    }

    open fun hasStetho(): Boolean {
        return false
    }

    open fun hasCrashlytics(): Boolean {
        return false
    }

    open fun hasAppIndex(): Boolean {
        return false
    }

    open fun hasUpdate(): Boolean {
        return false;
    }

    open fun hasRate(): Boolean {
        return false;
    }

    open fun hasAd(): Boolean {
        return false;
    }

    open fun hasColor(): Boolean {
        return false
    }

    open fun applyColor(): Boolean {
        return false
    }

    open fun hasTheme(): Boolean {
        return false
    }

    open fun hasRemoteUi(): Boolean {
        return false
    }

    open fun getRemoteUi(): Class<*>? {
        return null
    }

    open fun getAdmobAppId(): Int {
        return 0
    }

    open fun getScreen(): String {
        return javaClass.simpleName
    }

    open fun onOpen() {}

    open fun onClose() {}

    open fun onActivityOpen(activity: Activity) {}

    open fun onActivityClose(activity: Activity) {}

    open fun getName(): String? {
        return null
    }

    open fun getDescription(): String? {
        return null
    }

    open fun getUrl(): String? {
        return null
    }

    @SuppressLint("MissingPermission")
    fun getAnalytics(): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(this)
    }

/*    abstract fun getPrimaryColor(): Colorful.ThemeColor

    abstract fun getAccentColor(): Colorful.ThemeColor

    abstract fun isTranslucent(): Boolean

    abstract fun isDark(): Boolean*/

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //MultiDex.install(this)
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {

        if (isDebug() && hasStrict()) {
            setStrictMode()
        }
        super.onCreate()

        // CondomProcess.installInCurrentProcess(this)
        //context = CondomContext.wrap(this, packageName)

        if (isDebug() && hasLeakCanary()) {
            if (!initLeakCanary()) {
                return
            }
        }

        if (isDebug() && hasStetho()) {
            Stetho.initializeWithDefaults(this);
        }

        if (isDebug()) {
            Timber.plant(Timber.DebugTree())
        }
        if (hasCrashlytics()) {
/*            val fabric = Fabric.Builder(this)
                    .kits(Crashlytics())
                    .debuggable(true)
                    .build()
            Fabric.with(fabric)*/
        }
        configRx()
        configFresco()
        FirebaseApp.initializeApp(this)
        if (hasAd() && getAdmobAppId() != 0) {
            MobileAds.initialize(this, TextUtil.getString(this, getAdmobAppId()))
        }

        if (hasAppIndex()) {
            val name = getName()
            val description = getDescription()
            val url = getUrl()
            if (name != null && description != null && url != null) {
                action = getAction(description, url);
                indexable = Indexables.newSimple(name, url);
                startAppIndex();
            }
        }
        if (hasColor()) {
            color = ColorUtil.createColor(
                R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent
            )
        }

        registerActivityLifecycleCallbacks(this)
        TypefaceProvider.registerDefaultIconSets()
        onOpen()
        throwAnalytics(Constants.Event.APPLICATION, getScreen())
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTerminate() {
        stopAppIndex()
        onClose()
        super.onTerminate()
    }

    override fun onActivityPaused(activity: Activity) {
        visible = false
    }

    override fun onActivityResumed(activity: Activity) {
        visible = true
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        onActivityClose(activity)
        refs?.clear()
        if (hasUpdate()) {
            stopUpdate()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        onActivityClose(activity)
        refs = WeakReference(activity)
        goToRemoteUi()
        if (hasUpdate()) {
            startUpdate()
        }
    }

    fun isVisible(): Boolean {
        return visible
    }

    fun getCurrentUi(): Activity? {
        if (refs != null) {
            return refs?.get()
        } else {
            return null
        }
    }

    open fun getColor(): Color {
        return color;
    }

    open fun throwAnalytics(event: String, screen: String) {
        throwAnalytics(event, screen, null)
    }

    open fun throwAnalytics(event: String, screen: String, error: Throwable?) {
        if (AndroidUtil.isDebug(applicationContext)) {
            return
        }
        ex.postToNetwork({
            val packageName = AndroidUtil.getPackageName(applicationContext)
            val versionCode = AndroidUtil.getVersionCode(applicationContext)
            val versionName = AndroidUtil.getVersionName(applicationContext)
            val bundle = Bundle()
            bundle.putString(Constants.Param.PACKAGE_NAME, packageName)
            bundle.putInt(Constants.Param.VERSION_CODE, versionCode)
            bundle.putString(Constants.Param.VERSION_NAME, versionName)
            bundle.putString(Constants.Param.SCREEN, screen)
            error?.let {
                bundle.putString(Constants.Param.ERROR_MESSAGE, ExceptionUtils.getMessage(it))
                bundle.putString(Constants.Param.ERROR_DETAILS, ExceptionUtils.getStackTrace(it))
            }
            getAnalytics().logEvent(event, bundle)
        })
    }

    private fun goToRemoteUi() {
        val target = getRemoteUi();
        val current = getCurrentUi();
        if (hasRemoteUi() && target != null && current != null) {
            AndroidUtil.openActivity(getCurrentUi(), target, true)
        }
    }

    private fun setStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog().build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().detectActivityLeaks().build()
        )
    }

    private fun initLeakCanary(): Boolean {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return false
        }
        LeakCanary.install(this)
        return true
    }

    private fun configRx() {
        RxJavaPlugins.setErrorHandler {
            Timber.e(it, "Rx Global Error Handler.")
            throwAnalytics(Constants.Event.ERROR, getScreen(), it)
        }
    }

    private fun configFresco() {
        val diskSupplier = Supplier<File> { applicationContext.cacheDir }

        val diskCacheConfig =
            DiskCacheConfig.newBuilder(applicationContext).setBaseDirectoryName("image.cache")
                .setBaseDirectoryPathSupplier(diskSupplier).build()

        val frescoConfig =
            ImagePipelineConfig.newBuilder(this).setMainDiskCacheConfig(diskCacheConfig).build()

        Fresco.initialize(this, frescoConfig)
    }

    private fun startAppIndex() {
        if (isDebug()) {
            return
        }
        FirebaseAppIndex.getInstance().update(indexable)
        FirebaseUserActions.getInstance().start(action)
    }

    private fun stopAppIndex() {
        if (isDebug()) {
            return
        }
        FirebaseUserActions.getInstance().end(action)
    }

    private fun getAction(description: String, uri: String): Action {
        return Action.Builder(Action.Builder.VIEW_ACTION).setObject(description, uri).build()
    }

    private fun startUpdate() {

    }

    private fun stopUpdate() {

    }
}