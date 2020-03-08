package com.dreampany.tools.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.dreampany.common.misc.extension.icon
import com.dreampany.framework.api.service.BaseService
import com.dreampany.lockui.newui.LockView
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.AppMapper
import com.dreampany.tools.data.model.App
import com.dreampany.tools.data.source.pref.LockPref
import com.dreampany.tools.data.source.repository.AppRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 8/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LockService : BaseService(), LockView.Callback {


    @Inject
    internal lateinit var pref: LockPref

    @Inject
    internal lateinit var repo: AppRepository

    @Inject
    internal lateinit var mapper: AppMapper

    private var window: WindowManager? = null
    private var app: App? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var lockView: LockView? = null

    companion object {
        private val ACTION_LOCK = "action_lock"
        private val ACTION_UNLOCK = "action_unlock"
        private val EXTRA_PACKAGE = "extra_package"

        fun lockIntent(context: Context, pkg: String): Intent {
            val intent = Intent(context, LockService::class.java)
            intent.action = ACTION_LOCK
            intent.putExtra(EXTRA_PACKAGE, pkg)
            return intent
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: return Service.START_NOT_STICKY
        val pkg = intent.getStringExtra(EXTRA_PACKAGE) ?: return Service.START_NOT_STICKY
        when (action) {
            ACTION_LOCK -> {
                if (mapper.map.isEmpty) {
                    repo.getItems() // async loading
                    app = mapper.getItem(pkg)
                }
                showLock()
            }
            ACTION_UNLOCK -> {
                hideLock()
            }
        }
        return Service.START_NOT_STICKY
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onBackPressed() {

    }

    override fun onCorrect() {

    }

    override fun onPinCode(code: String) {

    }

    private fun showLock() {
        Timber.v("Showing Lock View as Alert Window")
        hideLock()
        try {
            lockView()
            window()?.addView(lockView, layoutParam())
        } catch (error: Throwable) {
            Timber.e(error)
        }
    }

    private fun hideLock() {
        try {
            if (lockView != null)
                window()?.removeView(lockView)
            lockView = null
        } catch (error: Throwable) {
            Timber.e(error)
        }
    }

    private fun lockView() {
        if (lockView == null) {
            lockView = LockView(this)
            lockView?.setCallback(this)
        }
        lockView?.reset()
        lockView?.setCode(pref.getPin())

        lockView?.setIcon(getIcon(this, app?.id))
        lockView?.setTitle(app?.name)

        lockView?.isFocusable = true
        lockView?.isFocusableInTouchMode = true
    }

    fun getIcon(context: Context, pkg: String?): Drawable? {
        var drawable = context.icon(pkg)
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_android_black_24dp)
        }
        return drawable
    }

    fun window(): WindowManager? {
        if (window == null) {
            window = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        }
        return window
    }

    private fun layoutParam(): WindowManager.LayoutParams? {
        if (layoutParams == null) {
            val LAYOUT_FLAG: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }
            layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_FULLSCREEN
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
            )
/*            layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_FULLSCREEN
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
            )*/
        }
        return layoutParams
    }
}