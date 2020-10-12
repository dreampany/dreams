package com.dreampany.framework.ui.fragment

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
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceFragmentCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.framework.R
import com.dreampany.framework.app.InjectApp
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.func.Executors
import com.dreampany.framework.ui.activity.BaseActivity
import com.dreampany.framework.ui.callback.Callback
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
    SearchView.OnQueryTextListener, Callback {

    @Inject
    protected lateinit var ex: Executors

    protected var savedInstanceState = false
    protected lateinit var menu: Menu
    protected var currentView: View? = null

    protected var activityCallback: Callback? = null
    protected var fragmentCallback: Callback? = null

    private var progress: KProgressHUD? = null
    private var sheetDialog: BottomSheetMaterialDialog? = null

    open val backPressed: Boolean = false

    @get:LayoutRes
    open val layoutRes: Int = 0

    @get:XmlRes
    open val prefLayoutRes: Int = 0

    @get:MenuRes
    open val menuRes: Int = 0

    @get:StringRes
    open val titleRes: Int = 0

    @get:IdRes
    open val searchMenuItemId: Int = 0

    @get:StringRes
    open val subtitleRes: Int = 0

    open val params: Map<String, Map<String, Any>?>? = null

    open fun onMenuCreated(menu: Menu) {}

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(menuRes != 0)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (prefLayoutRes != 0) {
            addPreferencesFromResource(prefLayoutRes)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (prefLayoutRes != 0) {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
        if (currentView != null) {
            currentView?.parent?.let { (it as ViewGroup).removeView(currentView) }
            return currentView
        }
        if (layoutRes != 0) {
            currentView = inflater.inflate(layoutRes, container, false)
        }
        //currentView!!.viewTreeObserver.addOnWindowFocusChangeListener(this)
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

        if (titleRes != 0) {
            setTitle(titleRes)
        }
        onStartUi(savedInstanceState)
        params?.let { app?.logEvent(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        if (menuRes != 0) { //this need clear
            menu.clear()
            inflater.inflate(menuRes, menu)
            onMenuCreated(menu)
            initSearch()
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

    override fun onSaveInstanceState(outState: Bundle) {
        savedInstanceState = true
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(inState: Bundle?) {
        super.onViewStateRestored(inState)
        savedInstanceState = false
    }

    override fun onRefresh() {}

    override fun onQueryTextChange(newText: String?): Boolean = false

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onTask(task: Task<*, *, *, *, *>) {}

    override fun <T> onItem(item: T) {}

    val parentRef: BaseActivity?
        get() {
            val ref = activity
            return if (ref is BaseActivity) ref else null
        }

    val toolbarRef: Toolbar?
        get() = parentRef?.toolbarRef

    val app : InjectApp?
        get() = parentRef?.app

    protected fun findMenuItemById(menuItemId: Int): MenuItem? = menu.findItem(menuItemId)

    protected fun getSearchMenuItem(): MenuItem? = findMenuItemById(searchMenuItemId)

    protected fun getSearchView(): SearchView? {
        val view = getSearchMenuItem()?.actionView ?: return null
        return view as SearchView
    }

    protected fun setTitle(@StringRes resId: Int) {
        if (activity is BaseActivity) {
            setTitle(resId)
        }
    }

    protected fun setTitle(title: String? = null) {
        if (activity is BaseActivity) {
            setTitle(title)
        }
    }

    protected fun setSubtitle(@StringRes resId: Int) {
        if (activity is BaseActivity) {
            setSubtitle(resId)
        }
    }

    protected fun setSubtitle(subtitle: String? = null) {
        if (activity is BaseActivity) {
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
        sheetDialog?.run { dismiss() }
        sheetDialog = null
    }

    private fun initSearch() {
        val searchView = getSearchView()
        searchView?.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_FILTER
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN
            //queryHint = getString(R.string.search)
            val searchManager =
                context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setOnQueryTextListener(this@BaseFragment)
            isIconified = false
        }
    }
}