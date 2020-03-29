package com.dreampany.common.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.XmlRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.preference.PreferenceFragmentCompat
import com.dreampany.common.R
import com.dreampany.common.data.model.Task
import com.dreampany.common.ui.activity.InjectActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseFragment : PreferenceFragmentCompat() {

    protected var fireOnStartUi: Boolean = true
    private lateinit var binding: ViewDataBinding
    protected var currentView: View? = null
    protected var task: Task<*, *, *>? = null
    protected var childTask: Task<*, *, *>? = null

    private var progress: KProgressHUD? = null
    private var sheetDialog: BottomSheetMaterialDialog? = null

    @LayoutRes
    open fun layoutRes(): Int = 0

    @XmlRes
    open fun prefLayoutRes(): Int = 0

    @StringRes
    open fun titleRes(): Int = 0

    @StringRes
    open fun subtitleRes(): Int = 0

    open fun backPressed(): Boolean = false

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val prefLayoutId = prefLayoutRes()
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
        val layoutId = layoutRes()
        if (layoutId != 0) {
            currentView = initLayout(layoutId, inflater, container, savedInstanceState)
        }
        //currentView!!.viewTreeObserver.addOnWindowFocusChangeListener(this)
        return currentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val titleResId = titleRes()
        if (titleResId != 0) {
            setTitle(titleResId)
        }

        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
        }
    }


    override fun onDestroyView() {
        hideProgress()
        hideDialog()
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

    protected fun showProgress() {
        if (progress == null) {
            progress = KProgressHUD.create(activity)
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
            activity?.run {
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