package com.dreampany.common.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.XmlRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.preference.PreferenceFragmentCompat
import com.dreampany.common.data.model.Task
import com.dreampany.common.ui.activity.InjectActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class InjectFragment : PreferenceFragmentCompat(), HasAndroidInjector {

    @Inject
    internal lateinit var injector: DispatchingAndroidInjector<Any>

    protected var fireOnStartUi: Boolean = true
    private lateinit var binding: ViewDataBinding
    protected var currentView: View? = null
    protected var task: Task<*, *, *>? = null
    protected var childTask: Task<*, *, *>? = null

    @LayoutRes
    open fun getLayoutId(): Int = 0

    @XmlRes
    open fun getPrefLayoutId(): Int = 0

    @StringRes
    open fun getTitleResId(): Int = 0

    @StringRes
    open fun getSubtitleResId(): Int = 0

    open fun backPressed(): Boolean = false

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun androidInjector(): AndroidInjector<Any> {
        return injector
    }

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
        currentView?.run {
            parent?.run {
                (this as ViewGroup).removeView(currentView)
            }
            return currentView
        }
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            currentView = initLayout(layoutId, inflater, container, savedInstanceState)
        }
        //currentView!!.viewTreeObserver.addOnWindowFocusChangeListener(this)
        return currentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val titleResId = getTitleResId()
        if (titleResId != 0) {
            setTitle(titleResId)
        }
    }

    protected fun <T : ViewDataBinding> getBinding(): T {
        return binding as T
    }

    protected fun setTitle(@StringRes resId: Int) {
        if (activity is InjectActivity) {
            setTitle(resId)
        }
    }

    protected fun setTitle(title: String? = null) {
        if (activity is InjectActivity) {
            setTitle(title)
        }
    }

    protected fun setSubtitle(@StringRes resId: Int) {
        if (activity is InjectActivity) {
            setSubtitle(resId)
        }
    }

    protected fun setSubtitle(subtitle: String? = null) {
        if (activity is InjectActivity) {
            setSubtitle(subtitle)
        }
    }

    private fun initLayout(
        @LayoutRes layoutId: Int,
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (layoutId != 0) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            binding.setLifecycleOwner(this)
            return binding.root
        } else {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
    }


}