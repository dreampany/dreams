package com.dreampany.common.ui.fragment

import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.annotation.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceFragmentCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.common.R
import com.dreampany.common.misc.func.Executors
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.common.ui.model.UiTask
import com.kaopiz.kprogresshud.KProgressHUD
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface
import javax.inject.Inject

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseFragment : PreferenceFragmentCompat(),
    SwipeRefreshLayout.OnRefreshListener,
    SearchView.OnQueryTextListener {

    @Inject
    protected lateinit var ex: Executors

    protected var fireOnStartUi: Boolean = true
    private lateinit var binding: ViewDataBinding
    private lateinit var menu: Menu

    protected var currentView: View? = null

    //protected var task: UiTask<*, *, *, *, *>? = null
    protected var childTask: UiTask<*, *, *, *, *>? = null

    private var progress: KProgressHUD? = null
    private var sheetDialog: BottomSheetMaterialDialog? = null

    open fun hasBinding(): Boolean = false

    @LayoutRes
    open fun layoutRes(): Int = 0

    @XmlRes
    open fun prefLayoutRes(): Int = 0

    @MenuRes
    open fun menuRes(): Int = 0

    @StringRes
    open fun titleRes(): Int = 0

    @IdRes
    open fun searchMenuItemId(): Int = 0

    @StringRes
    open fun subtitleRes(): Int = 0

    open fun backPressed(): Boolean = false

    open fun onMenuCreated(menu: Menu) {}

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(menuRes() != 0)
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
        } else {
            currentView = super.onCreateView(inflater, container, savedInstanceState)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        val menuRes = menuRes()
        if (menuRes != 0) { //this need clear
            menu.clear()
            inflater.inflate(menuRes, menu)
            binding.root.post {
                onMenuCreated(menu)
                initSearch()
            }
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

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                has
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }*/

    override fun onRefresh() {

    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    override fun onQueryTextSubmit(query: String?): Boolean = false

    protected fun findMenuItemById(menuItemId: Int): MenuItem? = menu.findItem(menuItemId)

    protected fun getSearchMenuItem(): MenuItem? = findMenuItemById(searchMenuItemId())

    protected fun getSearchView(): SearchView? {
        val view = getSearchMenuItem()?.actionView ?: return null
        return view as SearchView
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

    protected fun bindLocalCast(receiver: BroadcastReceiver, filter: IntentFilter) {
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver, filter)
        }
    }

    protected fun debindLocalCast(receiver: BroadcastReceiver) {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver)
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

    private fun initLayout(
        @LayoutRes layoutId: Int,
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (hasBinding()) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            binding.setLifecycleOwner(this)
            return binding.root
        } else {
            return inflater.inflate(layoutId, container, false)
        }
    }

    private fun initSearch() {
        val searchView = getSearchView()
        searchView?.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_FILTER
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN
            queryHint = getString(R.string.search)
            val searchManager =
                context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setOnQueryTextListener(this@BaseFragment)
            isIconified = false
        }
    }

    /*protected fun <F : Fragment, T : Task<*, *, *, *, *>> createFragment(clazz: KClass<F>, task: T): F {
        val instance = clazz.java.newInstance()
        if (instance.arguments == null) {
            val bundle = Bundle()
            bundle.putParcelable(Constants.Keys.TASK, task)
            instance.arguments = bundle
        } else {
            instance.arguments?.putParcelable(Constants.Keys.TASK, task)
        }
        return instance
    }*/
}