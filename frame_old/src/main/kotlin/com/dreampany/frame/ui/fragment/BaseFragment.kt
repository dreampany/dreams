package com.dreampany.frame.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceFragmentCompat
import com.dreampany.frame.app.BaseApp
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.Color
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.misc.AppExecutor
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.Animato
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseFragment : PreferenceFragmentCompat(), HasAndroidInjector,
    ViewTreeObserver.OnWindowFocusChangeListener, View.OnClickListener, TextWatcher {

    @Inject
    protected lateinit var ex: AppExecutor
    @Inject
    internal lateinit var childInjector: DispatchingAndroidInjector<Any>
    protected lateinit var binding: ViewDataBinding
    protected var task: Task<*>? = null
    protected var childTask: Task<*>? = null
    protected var currentView: View? = null
    protected var color: Color? = null
    protected var fireOnStartUi: Boolean = true
    //protected lateinit var activityCallback: UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>
    //protected lateinit var fragmentCallback: UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>

/*    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childInjector
    }*/

    override fun androidInjector(): AndroidInjector<Any> {
        return childInjector
    }

    open fun getLayoutId(): Int {
        return Constants.Default.INT
    }

    open fun getPrefLayoutId(): Int {
        return Constants.Default.INT
    }

    open fun getTitleResId(): Int {
        return Constants.Default.INT
    }

    open fun hasBackPressed(): Boolean {
        return false
    }

    open fun getCurrentFragment(): BaseFragment? {
        return this
    }

    protected abstract fun onStartUi(state: Bundle?)

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
        return currentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity
/*        if (BaseActivity::class.java.isInstance(activity) && UiCallback::class.java.isInstance(
                activity
            )
        ) {
            activityCallback =
                activity as UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>
        }*/

        // this will be worked when parent and child fragment relation
        val parentFragment = parentFragment
/*        if (BaseFragment::class.java.isInstance(parentFragment) && UiCallback::class.java.isInstance(
                parentFragment
            )
        ) {
            fragmentCallback =
                parentFragment as UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>
        }*/

        val titleResId = getTitleResId()
        if (titleResId != 0) {
            setTitle(titleResId)
        }

        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        if (this.userVisibleHint) {
            this.onVisible()
        }
    }

    override fun onPause() {
        this.onInvisible()
        super.onPause()
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onClick(v: View) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

    fun onVisible() {

    }

    fun onInvisible() {

    }

    fun getApp(): BaseApp? {
        return getParent()?.getApp()
    }

    fun getAppContext(): Context? {
        return context?.applicationContext
    }

    open fun getUiColor(): Color? {
        return color
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

    protected fun <T> getIntentValue(key: String): T? {
        val bundle = getBundle()
        return getIntentValue<T>(key, bundle)
    }

    protected fun <T> getIntentValue(key: String, bundle: Bundle?): T? {
        var t: T? = null
        if (bundle != null) {
            t = bundle.getParcelable<Parcelable>(key) as T?
        }
        if (bundle != null && t == null) {
            t = bundle.getSerializable(key) as T?
        }
        return t
    }

    protected fun getBundle(): Bundle? {
        return arguments
    }

    protected fun <T : Task<*>> getCurrentTask(intent: Intent): T? {
        val task = getIntentValue<T>(Constants.Task.TASK, intent.extras)
        return task
    }

    protected fun <T : Task<*>> getCurrentTask(): T? {
        return getCurrentTask(false)
    }

    protected fun <T : Task<*>> getCurrentTask(freshTask: Boolean): T? {
        if (task == null || freshTask) {
            task = getIntentValue<T>(Constants.Task.TASK)
        }
        return task as T?
    }

    protected fun <T : Base> getInput(): T? {
        val task: Task<*>? = getCurrentTask()
        return task?.input as T?
    }

    protected fun <T : View> findViewById(@IdRes id: Int): T? {
        var current = currentView
        if (current == null) {
            current = view
        }
        return if (current != null) current.findViewById(id) else null
    }

    @SuppressLint("ResourceType")
    protected fun setTitle(@StringRes resId: Int) {
        if (resId <= 0) {
            return
        }
        //setTitle(context?.let { TextUtil.getString(it, resId) })
    }

    @SuppressLint("ResourceType")
    protected fun setSubtitle(@StringRes resId: Int) {
        if (resId <= 0) {
            return
        }
        //setSubtitle(context?.let { TextUtil.getString(it, resId) })
    }

    protected fun setTitle(title: String?) {
        val activity = activity
        if (BaseActivity::class.java.isInstance(activity)) {
            (activity as BaseActivity).setTitle(title)
        }
    }

    protected fun setSubtitle(subtitle: String? = null) {
        val activity = activity
        if (BaseActivity::class.java.isInstance(activity)) {
            (activity as BaseActivity).setSubtitle(subtitle)
        }
    }

    protected fun bindLocalCast(receiver: BroadcastReceiver, filter: IntentFilter) {
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver, filter)
    }

    protected fun debindLocalCast(receiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver)
    }

    fun openActivity(target: Class<*>) {
        AndroidUtil.openActivity(this, target)
    }

    fun openActivity(target: Class<*>, requestCode: Int) {
        AndroidUtil.openActivity(this, target, requestCode)
    }

    fun openActivity(target: Class<*>, task: Task<*>) {
        AndroidUtil.openActivity(this, target, task)
    }

    fun openActivity(target: Class<*>, task: Task<*>, requestCode: Int) {
        AndroidUtil.openActivity(this, target, task, requestCode)
    }

    fun showInfo(info: String) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.showInfo(info)
    }

    protected fun showError(error: String) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.showError(error)
    }

    fun showAlert(title: String, text: String, backgroundColor: Int, timeout: Long) {
        showAlert(title, text, backgroundColor, timeout, null)
    }

    fun showAlert(
        title: String,
        text: String,
        backgroundColor: Int,
        timeout: Long,
        listener: View.OnClickListener?
    ) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.showAlert(title, text, backgroundColor, timeout, listener)
    }

    fun hideAlert() {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.hideAlert()
    }

    protected fun forResult(okay: Boolean = true) {
        val task = getCurrentTask<Task<*>>(false)
        forResult(task, okay)
    }

    protected fun forResult(task: Task<*>? = null, okay: Boolean = true) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        val intent = Intent()
        intent.putExtra(Constants.Task.TASK, task as Parcelable)
        if (okay) {
            parent?.setResult(Activity.RESULT_OK, intent)
        } else {
            parent?.setResult(Activity.RESULT_CANCELED, intent)
        }
        parent?.run {
            finish()
            Animato.animateSlideRight(this)
        }
    }

    protected fun isOkay(resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK
    }

/*    protected fun showProgress(message: String) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.showProgress(message)
    }

    protected fun hideProgress() {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.hideProgress()
    }*/
}