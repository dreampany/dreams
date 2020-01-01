package com.dreampany.simple.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dreampany.simple.R
import com.dreampany.simple.app.BaseApp
import com.dreampany.simple.data.model.Task
import com.dreampany.simple.misc.AppExecutor
import com.dreampany.simple.misc.Constants
import com.dreampany.simple.ui.fragment.BaseFragment
import com.dreampany.simple.util.AndroidUtil
import com.dreampany.simple.util.Animato
import com.dreampany.simple.util.FragmentUtil
import com.dreampany.simple.util.NotifyUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tapadoo.alerter.Alerter
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.reflect.KClass


/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity : DaggerAppCompatActivity(), View.OnClickListener {

    @Inject
    protected lateinit var ex: AppExecutor
    protected lateinit var binding: ViewDataBinding
    protected var toolbar: Toolbar? = null
    protected var task: Task<*>? = null
    protected var childTask: Task<*>? = null
    protected var currentFragment: BaseFragment? = null
    protected var fireOnStartUi: Boolean = true
    private var doubleBackToExitPressedOnce: Boolean = false

    open fun getLayoutId(): Int {
        return 0
    }

    open fun getToolbarId(): Int {
        return 0
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

    open fun hasBackPressed(): Boolean {
        return false
    }

    open fun hasDoubleBackPressed(): Boolean {
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
        val app = getApp()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            initLayout(layoutId)
            initToolbar()
        }
        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
        }
        var root = isTaskRoot
        //Timber.v("%s RootTask %s", getScreen(), root)
    }

    override fun onDestroy() {
        val app = getApp();
        hideAlert()
        onStopUi()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentFragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (hasBackPressed()) {
            return
        }

        val fragment = currentFragment
        if (fragment != null && fragment.hasBackPressed()) {
            return
        }
/*        val manager = supportFragmentManager;
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        }*/
        if (hasDoubleBackPressed()) {
            if (doubleBackToExitPressedOnce) {
                //super.onBackPressed()
                finish()
                Animato.animateSlideRight(this)
                return;
            }
            doubleBackToExitPressedOnce = true
            //NotifyUtil.shortToast(this, R.string.back_pressed)
            ex.postToUi(Runnable{ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            finish()
            Animato.animateSlideRight(this)
        }
    }

    override fun onClick(view: View) {

    }

    fun getToolbarRef(): Toolbar? {
        return toolbar
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
            //BarUtil.hide(this)
        } else {
            //BarUtil.show(this)
        }
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    private fun initToolbar() {
        toolbar = findViewById<Toolbar>(getToolbarId())
        if (toolbar != null) {
            if (isFullScreen()) {
                if (toolbar!!.isShown) {
                    toolbar!!.visibility = View.GONE
                }
            } else {
                if (!toolbar!!.isShown) {
                    toolbar!!.visibility = View.VISIBLE
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

    protected fun <T : Task<*>> getCurrentTask(freshTask: Boolean): T? {
        if (task == null || freshTask) {
            task = getIntentValue<T>(Constants.Task.TASK)
        }
        return task as T?
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
        return intent.extras
    }

    override fun setTitle(titleId: Int) {
        if (titleId <= 0) {
            return
        }
        //setSubtitle(TextUtil.getString(this, titleId))
    }

    fun setSubtitle(subtitleId: Int) {
        if (subtitleId <= 0) {
            return
        }
        //setSubtitle(TextUtil.getString(this, subtitleId))
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

    fun <T : Any> openActivity(target: KClass<T>, finish: Boolean) {
        openActivity(target.java, finish)
    }

    fun openActivity(target: Class<*>, finish: Boolean) {
        AndroidUtil.openActivity(this, target, finish)
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

    fun openActivity(target: Class<*>, task: Task<*>, finish: Boolean) {
        AndroidUtil.openActivity(this, target, task, finish)
    }

    protected fun <T : BaseFragment> commitFragment(
        clazz: Class<T>,
        fragmentProvider: Lazy<T>,
        parentId: Int
    ): T? {
        var fragment: T? = FragmentUtil.getFragmentByTag(this, clazz.simpleName)
        if (fragment == null) {
            //Timber.v("New Fragment Created %s", clazz)
            fragment = fragmentProvider.get()
        }
        val currentFragment = FragmentUtil.commitFragment<T>(ex, this, fragment, parentId)
        this.currentFragment = currentFragment
        return currentFragment
    }

    protected fun <T : BaseFragment> commitFragment(
        clazz: Class<T>,
        fragmentProvider: Lazy<T>,
        parentId: Int,
        task: Task<*>
    ): T? {
        var fragment: T? = FragmentUtil.getFragmentByTag(this, clazz.simpleName)
        if (fragment == null) {
            fragment = fragmentProvider.get()
            val bundle = Bundle()
            bundle.putParcelable(Constants.Task.TASK, task)
            fragment!!.arguments = bundle
        } else {
            fragment.arguments!!.putParcelable(Constants.Task.TASK, task)
        }

        val currentFragment = FragmentUtil.commitFragment(ex, this, fragment, parentId)
        this.currentFragment = currentFragment
        return currentFragment
    }

    fun checkPermissions(vararg permissions: String, listener: MultiplePermissionsListener) {
        if (!isAlive()) {
            return
        }
        Dexter.withActivity(this)
            .withPermissions(*permissions)
            .withListener(listener)
            .check()
    }

    fun showInfo(info: String) {
        if (!isAlive()) {
            return
        }
        //NotifyUtil.showInfo(this, info)
    }

    fun showError(error: String) {
        if (!isAlive()) {
            return
        }
        //NotifyUtil.showInfo(this, error)
    }

    fun showAlert(title: String, text: String, @ColorRes backgroundColor: Int, timeout: Long) {
        showAlert(title, text, backgroundColor, timeout, null)
    }

    fun showAlert(
        title: String,
        text: String,
        @ColorRes backgroundColor: Int,
        timeout: Long,
        listener: View.OnClickListener?
    ) {
        hideAlert()
        val alerter = Alerter.create(this)
            .setTitle(title)
            .setText(text)
            .setBackgroundColorRes(backgroundColor)
            .setIcon(R.drawable.alerter_ic_face)
            .setDuration(timeout)
            .enableSwipeToDismiss();
        if (listener != null) {
            alerter.setOnClickListener(listener)
        }
        alerter.show()
    }

    fun hideAlert() {
        if (Alerter.isShowing) {
            Alerter.hide()
        }
    }

/*    fun showProgress(message: String) {
        if (progress == null) {
            progress = ProgressDialog(this)
            progress?.setCancelable(true)
        }

        if (progress?.isShowing()!!) {
            return
        }
        progress?.let {
            it.setMessage(message + "...")
            it.show()
        }
    }

    fun hideProgress() {
        progress?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }*/
}