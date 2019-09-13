package com.dreampany.tools.app

import android.Manifest
import android.app.Activity
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.crashlytics.android.Crashlytics
import com.dreampany.framework.app.BaseApp
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.R
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.injector.app.DaggerAppComponent
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.service.NotifyService
import com.dreampany.framework.misc.SmartAd
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.tools.service.AppService
import com.dreampany.tools.ui.activity.LaunchActivity
import com.dreampany.tools.ui.activity.NavigationActivity
import com.dreampany.tools.worker.NotifyWorker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class App : BaseApp() {

    @Inject
    lateinit var pref: Pref

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG;
    }

    override fun hasCrashlytics(): Boolean {
        return true
    }

    override fun hasAppIndex(): Boolean {
        return true
    }

    override fun hasUpdate(): Boolean {
        return true
    }

    override fun hasRate(): Boolean {
        return true
    }

    override fun hasAd(): Boolean {
        return true
    }

    override fun hasColor(): Boolean {
        return true
    }

    override fun applyColor(): Boolean {
        return true
    }

    override fun getAdmobAppId(): Int {
        return R.string.admob_app_id
    }

    override fun onCreate() {
        super.onCreate()
        if (!isDebug() && hasCrashlytics()) {
            configFabric()
        }
        configAd()
        //configService()
        //configJob()
        configWork()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onActivityOpen(activity: Activity) {
        super.onActivityOpen(activity)
        if (activity is NavigationActivity) {
/*            activity.runWithPermissions(Permission) {
                createCameraSource()
            }*/
            if (AndroidUtil.hasPie()) {
                Dexter.withActivity(activity)
                    .withPermission(Manifest.permission.FOREGROUND_SERVICE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            configService()
                        }
                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                        }
                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        }

                    })
                    .check()
            } else {
                configService()
            }
        }
    }

    override fun onActivityClose(activity: Activity) {
        super.onActivityClose(activity)
    }

    private fun configFabric() {
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(isDebug())
            .build()
        Fabric.with(fabric)
    }

    private fun configAd() {
        //ad.initPoints(Util.AD_POINTS)
        val config = SmartAd.Config.Builder()
            .bannerExpireDelay(TimeUnit.MINUTES.toMillis(1))
            .interstitialExpireDelay(TimeUnit.MINUTES.toMillis(10))
            .rewardedExpireDelay(TimeUnit.MINUTES.toMillis(30))
            .enabled(!isDebug())
        ad.setConfig(config.build())
    }

    private fun configService() {
        service.openService(AppService::class.java)
    }

    private fun configJob() {
        if (pref.hasNotification()) {
            job.create(
                Constants.Tag.NOTIFY_SERVICE,
                NotifyService::class,
                Constants.Delay.Notify.toInt(),
                Constants.Period.Notify.toInt()
            )
        } else {
            job.cancel(Constants.Tag.NOTIFY_SERVICE)
        }
    }

    /**
     * java.lang.IllegalArgumentException: could not find worker: androidx.work.impl.workers.ConstraintTrackingWorker
     * at com.dreampany.frame.worker.factory.WorkerInjectorFactory.createWorker(WorkerInjectorFactory.kt:26)
     */
    private fun configWork() {
        if (pref.hasNotification()) {
            worker.createPeriodic(NotifyWorker::class, Constants.Period.Notify, TimeUnit.MILLISECONDS)
        } else {
            worker.cancel(NotifyWorker::class)
        }
    }


    private fun isVersionUpgraded(): Boolean {
        val exists = pref.getVersionCode()
        val current = AndroidUtil.getVersionCode(this)
        if (current != exists) {
            return true
        }
        return false
    }

    private fun clean() {
        if (isVersionUpgraded()) {
            val exists = pref.getVersionCode()
            val current = AndroidUtil.getVersionCode(this)

            when (current) {
                45 -> {
                    if (exists < 44) {

                    }
                }

            }
            pref.setVersionCode(current)
        }
    }
}