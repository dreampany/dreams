package com.dreampany.common.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.dreampany.common.app.BaseApp
import com.dreampany.common.misc.AppExecutors
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.common.util.AndroidUtil
import com.dreampany.common.util.TextUtil
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import hugo.weaving.DebugLog
import javax.inject.Inject

/**
 * Created by Roman-372 on 5/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseFragment : PreferenceFragmentCompat(), HasSupportFragmentInjector {

    @Inject
    protected lateinit var ex: AppExecutors
    protected lateinit var binding: ViewDataBinding
    @Inject
    internal lateinit var childInjector: DispatchingAndroidInjector<Fragment>
    protected var fireOnStartUi: Boolean = true

    protected var currentView: View? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childInjector
    }

    open fun getLayoutId(): Int {
        return 0
    }

    open fun getPrefLayoutId(): Int {
        return 0
    }

    open fun hasBackPressed(): Boolean {
        return false
    }

    open fun getCurrentFragment(): BaseFragment? {
        return this
    }

    @DebugLog
    protected abstract fun onStartUi(state: Bundle?)

    @DebugLog
    protected abstract fun onStopUi()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val prefLayoutId = getPrefLayoutId()
        if (prefLayoutId != 0) {
            addPreferencesFromResource(prefLayoutId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (currentView != null) {
            if (currentView?.getParent() != null) {
                (currentView?.getParent() as ViewGroup).removeView(currentView)
            }
            return currentView
        }
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            binding.setLifecycleOwner(this)
            currentView = binding.root
        } else {
            currentView = super.onCreateView(inflater, container, savedInstanceState)
        }
        //currentView!!.viewTreeObserver.addOnWindowFocusChangeListener(this)
        return currentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
        }
    }

    override fun onDestroyView() {
        onStopUi()
        if (currentView != null) {
            //currentView!!.viewTreeObserver.removeOnWindowFocusChangeListener(this)
            val parent = currentView?.parent
            if (parent != null) {
                (parent as ViewGroup).removeAllViews()
            }
        }
        super.onDestroyView()
    }

    override fun getContext(): Context? {
        if (AndroidUtil.hasMarshmallow()) {
            return super.getContext()
        }
        return if (currentView != null) {
            currentView?.context
        } else {
            getParent()
        }
    }

    fun getApp(): BaseApp? {
        return getParent()?.getApp()
    }

    protected fun forResult() {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        val intent = Intent()
        parent?.setResult(Activity.RESULT_OK, intent)
        parent?.finish()
    }

    @SuppressLint("ResourceType")
    protected fun setTitle(@StringRes resId: Int) {
        if (resId <= 0) {
            return
        }
        setTitle(context?.let { TextUtil.getString(it, resId) })
    }

    @SuppressLint("ResourceType")
    protected fun setSubtitle(@StringRes resId: Int) {
        if (resId <= 0) {
            return
        }
        setSubtitle(context?.let { TextUtil.getString(it, resId) })
    }

    protected fun setTitle(title: String?) {
        val activity = activity
        if (BaseActivity::class.java.isInstance(activity)) {
            (activity as BaseActivity).setTitle(title)
        }
    }

    protected fun setSubtitle(subtitle: String?) {
        val activity = activity
        if (BaseActivity::class.java.isInstance(activity)) {
            (activity as BaseActivity).setSubtitle(subtitle)
        }
    }

    protected fun getParent(): BaseActivity? {
        val activity = activity
        return if (BaseActivity::class.java.isInstance(activity) && AndroidUtil.isAlive(activity)) {
            activity as BaseActivity
        } else {
            null
        }
    }

    protected fun isParentAlive(): Boolean {
        return AndroidUtil.isAlive(getParent())
    }
}