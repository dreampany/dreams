package com.dreampany.framework.ui.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.framework.R
import com.dreampany.framework.app.InjectApp
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.fragment
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.func.Executors
import com.dreampany.framework.misc.util.NotifyUtil
import com.dreampany.framework.ui.callback.Callback
import com.dreampany.framework.ui.fragment.BaseFragment
import com.google.android.material.appbar.MaterialToolbar
import com.kaopiz.kprogresshud.KProgressHUD
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface
import dagger.Lazy
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener,
    SearchView.OnQueryTextListener, Callback {

    @Inject
    protected lateinit var ex: Executors

    protected var startByBase: Boolean = true
    private var doubleBackPressedOnce: Boolean = false

    protected var toolbar: MaterialToolbar? = null
    protected var menu: Menu? = null

    //protected var task: Task<*, *, *, *, *>? = null

    protected var fragment: BaseFragment? = null

    private var progress: KProgressHUD? = null
    private var sheetDialog: BottomSheetMaterialDialog? = null

    open val fullScreen: Boolean = false

    open val homeUp: Boolean = false

    open val backPressed: Boolean = false

    open val doubleBackPressed: Boolean = false

    @get:LayoutRes
    open val layoutRes: Int = 0

    @get:MenuRes
    open val menuRes: Int = 0

    @get:IdRes
    open val toolbarId: Int = 0

    @get:IdRes
    open val searchMenuItemId: Int = 0

    @get:StringRes
    open val titleRes: Int = 0

    @get:StringRes
    open val subtitleRes: Int = 0

    open val params: Map<String, Map<String, Any>?>? = null

    open fun onMenuCreated(menu: Menu) {}

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (fullScreen)
            requestWindowFeature(Window.FEATURE_NO_TITLE)

        if (startByBase && layoutRes != 0) {
            setContentView(layoutRes)
            initToolbar()
        }
        if (startByBase) {
            onStartUi(savedInstanceState)
            params?.let { app.logEvent(it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        if (menuRes != 0) { //this need clear
            menu.clear()
            menuInflater.inflate(menuRes, menu)
            ex.postToUi(Runnable {
                onMenuCreated(menu)
                initSearch()
            })
            return true
        }
        return super.onCreateOptionsMenu(menu)
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

    override fun onBackPressed() {
        val searchOpened = getSearchView()?.isIconified ?: false
        if (searchOpened) {
            getSearchView()?.isIconified = false
            return
        }

        if (backPressed) return
        if (fragment?.backPressed ?: false) return

        if (doubleBackPressed) {
            if (doubleBackPressedOnce) {
                finish()
                return
            }
            doubleBackPressedOnce = true
            NotifyUtil.shortToast(this, R.string.back_pressed)
            ex.postToUi(Runnable { doubleBackPressedOnce = false }, 2000)
            return
        }
        finish()
    }

    override fun onRefresh() {

    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onTask(task: Task<*, *, *, *, *>) {}

    override fun <T> onInput(item: T) {

    }

    val toolbarRef: Toolbar?
        get() = toolbar

    val app : InjectApp
        get() = application as InjectApp

    protected fun setSubtitle(@StringRes subtitleRes: Int) {
        if (subtitleRes != 0) {
            setSubtitle(getString(subtitleRes))
        }
    }

    protected fun setSubtitle(subtitle: String?) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.subtitle = subtitle
        }
    }

    protected fun findMenuItemById(menuItemId: Int): MenuItem? = menu?.findItem(menuItemId)

    protected fun getSearchMenuItem(): MenuItem? = findMenuItemById(searchMenuItemId)

    protected fun getSearchView(): SearchView? {
        val view = getSearchMenuItem()?.actionView ?: return null
        return view as SearchView
    }

    /*protected fun getBundle(): Bundle? {
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

    protected fun getTask(freshTask: Boolean = false): T? {
        if (task == null || freshTask) {
            task = getIntentValue<T>(Constants.Keys.TASK)
        }
        return task as T?
    }*/

    protected fun <T : BaseFragment> commitFragment(
        classOfT: KClass<T>, provider: Lazy<T>, @IdRes parent: Int
    ) {
        var fragment: T? = fragment<T>(classOfT.simpleName)
        if (fragment == null) {
            fragment = provider.get()
        }
        open(fragment, parent, ex)
        this.fragment = fragment
    }

    /* protected fun <T : Fragment> createFragment(clazz: KClass<T>, task: Task<*, *, *, *, *>): T {
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

    protected fun progress(progress : Boolean) {
        if (progress) showProgress() else hideProgress()
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
            val builder = BottomSheetMaterialDialog.Builder(this)
                .setTitle(getString(titleRes))
                .setMessage(
                    message
                        ?: if (messageRes != 0) getString(messageRes) else Constant.Default.STRING
                )
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
            sheetDialog = builder.build()
        }
        sheetDialog?.show()
    }

    protected fun hideDialog() {
        sheetDialog?.run {
            dismiss()
        }
        sheetDialog = null
    }

    private fun initLayout(@LayoutRes layoutRes: Int) {
        /*if (hasBinding) {

        } else {

        }*/
    }

    protected fun initToolbar() {
        if (toolbarId != 0) {
            toolbar = findViewById<MaterialToolbar>(toolbarId)
            setSupportActionBar(toolbar)
            if (homeUp) {
                val actionBar = supportActionBar
                actionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                }
            }
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
            setSearchableInfo(searchManager.getSearchableInfo(this@BaseActivity.componentName))
            setOnQueryTextListener(this@BaseActivity)
            isIconified = false
        }
    }
}