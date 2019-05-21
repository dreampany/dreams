package com.dreampany.common.ui.activity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dreampany.common.R
import com.dreampany.common.app.BaseApp
import com.dreampany.common.misc.AppExecutors
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.common.util.AndroidUtil
import com.dreampany.common.util.FragmentUtil
import com.dreampany.common.util.TextUtil
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by Roman-372 on 5/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    protected lateinit var ex: AppExecutors
    protected lateinit var binding: ViewDataBinding
    protected var fireOnStartUi: Boolean = true
    protected var currentFragment: BaseFragment? = null

    open fun getLayoutId(): Int {
        return 0
    }

    open fun getToolbarId(): Int {
        return R.id.toolbar
    }

    open fun isFullScreen(): Boolean {
        return false
    }

    open fun isHomeUp(): Boolean {
        return true
    }

    open fun isScreenOn(): Boolean {
        return false
    }

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AndroidUtil.hasLollipop()) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }
        super.onCreate(savedInstanceState)
        if (isScreenOn()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            initLayout(layoutId)
            initToolbar()
        }
        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
        }
    }

    override fun onDestroy() {
        onStopUi()
        super.onDestroy()
    }

    fun isAlive(): Boolean {
        return AndroidUtil.isAlive(this)
    }

    fun getApp(): BaseApp {
        return application as BaseApp
    }

    private fun initLayout(layoutId: Int) {
        if (isFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(getToolbarId())
        if (toolbar != null) {
            if (isFullScreen()) {
                if (toolbar.isShown) {
                    toolbar.visibility = View.GONE
                }
            } else {
                if (!toolbar.isShown) {
                    toolbar.visibility = View.VISIBLE
                }
                setSupportActionBar(toolbar)
                if (isHomeUp()) {
                    val actionBar = supportActionBar
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true)
                        actionBar.setHomeButtonEnabled(true)
                    }
                }
            }
        }
    }

    override fun setTitle(titleId: Int) {
        if (titleId <= 0) {
            return
        }
        setSubtitle(TextUtil.getString(this, titleId))
    }

    fun setSubtitle(subtitleId: Int) {
        if (subtitleId <= 0) {
            return
        }
        setSubtitle(TextUtil.getString(this, subtitleId))
    }

    fun setTitle(title: String?) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = title
        }
    }

    fun setSubtitle(subtitle: String?) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.subtitle = subtitle
        }
    }

    fun <T : Any> openActivity(target: KClass<T>) {
        openActivity(target.java)
    }

    fun openActivity(target: Class<*>) {
        AndroidUtil.openActivity(this, target)
    }

    protected fun <T : BaseFragment> commitFragment(
        clazz: Class<T>, fragmentProvider: Lazy<T>, parentId: Int
    ): T {
        var fragment: T? = FragmentUtil.getFragmentByTag(this, clazz.simpleName)
        if (fragment == null) {
            fragment = fragmentProvider.get()
        }
        val currentFragment = FragmentUtil.commitFragment<T>(this, fragment!!, parentId, ex)
        this.currentFragment = currentFragment
        return currentFragment
    }
}