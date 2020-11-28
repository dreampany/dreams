package com.dreampany.framework.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import com.dreampany.framework.R
import com.dreampany.framework.app.InjectApp
import com.dreampany.framework.misc.func.Executors
import com.dreampany.framework.ui.activity.BaseActivity
import com.dreampany.framework.ui.callback.Callback
import javax.inject.Inject

/**
 * Created by roman on 11/28/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseDialogFragment : AppCompatDialogFragment() {

    @Inject
    protected lateinit var ex: Executors

    protected var savedInstanceState = false
    protected val isFinishing : Boolean get() = activity?.isFinishing ?: savedInstanceState
    protected var currentView: View? = null

    protected var activityCallback: Callback? = null
    protected var fragmentCallback: Callback? = null

    @get:LayoutRes
    open val layoutRes: Int = 0

    open val params: Map<String, Map<String, Any>?>? = null

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Style_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (currentView != null) {
            currentView?.parent?.let { (it as ViewGroup).removeView(currentView) }
            return currentView
        }
        if (layoutRes != 0) {
            currentView = inflater.inflate(layoutRes, container, false)
        }
        return currentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            if (it is Callback) activityCallback = it
        }

        parentFragment?.let {
            if (it is Callback) fragmentCallback = it
        }

        onStartUi(savedInstanceState)
        params?.let { app?.logEvent(it) }
    }

    override fun onDestroyView() {
        //hideProgress()
        //hideDialog()
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

    override fun onSaveInstanceState(outState: Bundle) {
        savedInstanceState = true
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(inState: Bundle?) {
        super.onViewStateRestored(inState)
        savedInstanceState = false
    }

    val parentRef: BaseActivity?
        get() {
            val ref = activity
            return if (ref is BaseActivity) ref else null
        }

    val app : InjectApp?
        get() = parentRef?.app

    fun show(activity: BaseActivity) {
        show(activity.supportFragmentManager, activity.toString())
    }

    fun show(fragment: BaseFragment) {
        show(fragment.childFragmentManager, fragment.tag)
    }

    fun hide() {
        dismissAllowingStateLoss()
    }
}