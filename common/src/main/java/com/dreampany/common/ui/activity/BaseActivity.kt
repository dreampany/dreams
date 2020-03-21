package com.dreampany.common.ui.activity

import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dreampany.common.R
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.func.Executors
import com.dreampany.common.ui.fragment.InjectFragment
import com.kaopiz.kprogresshud.KProgressHUD
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface
import javax.inject.Inject

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    protected lateinit var ex: Executors

    protected var fireOnStartUi: Boolean = true
    private lateinit var binding: ViewDataBinding
    private lateinit var toolbar: Toolbar
    protected var task: Task<*, *, *>? = null

    protected var fragment: InjectFragment? = null

    private var progress: KProgressHUD? = null
    private var sheetDialog: BottomSheetMaterialDialog? = null

    open fun fullScreen(): Boolean = false

    open fun hasBinding(): Boolean = false

    @LayoutRes
    open fun layoutId(): Int = 0

    @IdRes
    open fun toolbarId(): Int = 0

    open fun homeUp(): Boolean = false

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val layoutId = layoutId()
        if (layoutId != 0) {
            initLayout(layoutId)
            initToolbar()
        }
        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        hideProgress()
        hideDialog()
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

    protected fun <T : ViewDataBinding> getBinding(): T {
        return binding as T
    }

    private fun initLayout(@LayoutRes layoutId: Int) {
        if (fullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        if (hasBinding()) {
            binding = DataBindingUtil.setContentView(this, layoutId)
            binding.setLifecycleOwner(this)
        } else {
            setContentView(layoutId)
        }
    }

    private fun initToolbar() {
        val toolbarId = toolbarId()
        if (toolbarId != 0) {
            toolbar = findViewById<Toolbar>(toolbarId)
            setSupportActionBar(toolbar)
            if (homeUp()) {
                val actionBar = supportActionBar
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true)
                    actionBar.setHomeButtonEnabled(true)
                }
            }
        }

    }

    protected fun getBundle(): Bundle? {
        return intent.extras
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

    protected fun <T> getIntentValue(key: String): T? {
        val bundle = getBundle()
        return getIntentValue<T>(key, bundle)
    }

    protected fun <T : Task<*, *, *>> getTask(freshTask: Boolean = false): T? {
        if (task == null || freshTask) {
            task = getIntentValue<T>(Constants.Keys.TASK)
        }
        return task as T?
    }

    protected fun showProgress() {
        if (progress == null) {
            progress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
        }
        progress?.show()
    }

    protected fun hideProgress() {
        progress?.run {
            if (isShowing) dismiss()
        }
    }

    protected fun showDialogue(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int = 0,
        message: String? = null,
        @StringRes positiveTitleRes: Int = R.string.ok,
        @DrawableRes positiveIconRes: Int = R.drawable.ic_done_black_24dp,
        @StringRes negativeTitleRes: Int = R.string.cancel,
        @DrawableRes negativeIconRes: Int = R.drawable.ic_close_black_24dp,
        cancellable: Boolean = false,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit
    ) {
        if (sheetDialog == null) {
            sheetDialog = BottomSheetMaterialDialog.Builder(this)
                .setTitle(getString(titleRes))
                .setMessage(message ?: getString(messageRes))
                .setCancelable(cancellable)
                .setPositiveButton(
                    getString(positiveTitleRes),
                    positiveIconRes,
                    { dialog: DialogInterface, which: Int ->
                        onPositiveClick()
                        dialog.dismiss()
                        sheetDialog = null
                    })
                .setNegativeButton(
                    getString(negativeTitleRes),
                    negativeIconRes,
                    { dialog: DialogInterface, which: Int ->
                        onNegativeClick()
                        dialog.dismiss()
                        sheetDialog = null
                    })
                .build()
        }
        sheetDialog?.show()
    }

    protected fun hideDialog() {
        sheetDialog?.run {
            dismiss()
        }
        sheetDialog = null
    }
}