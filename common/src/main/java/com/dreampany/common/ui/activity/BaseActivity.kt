package com.dreampany.common.ui.activity

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
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.common.R
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.extension.fragment
import com.dreampany.common.misc.extension.open
import com.dreampany.common.misc.func.Executors
import com.dreampany.common.misc.util.NotifyUtil
import com.dreampany.common.ui.callback.Callback
import com.dreampany.common.ui.fragment.BaseFragment
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

    protected var fireOnStartUi: Boolean = true
    private var doubleBackPressedOnce: Boolean = false

    private lateinit var binding: ViewDataBinding
    private lateinit var toolbar: Toolbar
    private lateinit var menu: Menu

    //protected var task: Task<*, *, *, *, *>? = null

    protected var fragment: BaseFragment? = null

    private var progress: KProgressHUD? = null
    private var sheetDialog: BottomSheetMaterialDialog? = null

    open fun fullScreen(): Boolean = false

    open fun homeUp(): Boolean = false

    open fun backPressed(): Boolean = false

    open fun doubleBackPressed(): Boolean = false

    open fun hasBinding(): Boolean = false

    @LayoutRes
    open fun layoutRes(): Int = 0

    @MenuRes
    open fun menuRes(): Int = 0

    @IdRes
    open fun toolbarId(): Int = 0

    @IdRes
    open fun searchMenuItemId(): Int = 0

    @StringRes
    open fun titleRes(): Int = 0

    @StringRes
    open fun subtitleRes(): Int = 0

    open fun onMenuCreated(menu: Menu) {}

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val layoutRes = layoutRes()
        if (layoutRes != 0) {
            initLayout(layoutRes)
            initToolbar()
        }
        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        val menuRes = menuRes()
        if (menuRes != 0) { //this need clear
            menu.clear()
            menuInflater.inflate(menuRes, menu)
            binding.root.post {
                onMenuCreated(menu)
                initSearch()
            }
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

        if (backPressed()) return
        if (fragment?.backPressed() ?: false) return

        if (doubleBackPressed()) {
            if (doubleBackPressedOnce) {
                finish()
                return
            }
            doubleBackPressedOnce = true
            NotifyUtil.shortToast(this, R.string.back_pressed)
            ex.postToUi(Runnable{ doubleBackPressedOnce = false }, 2000)
            return
        }
        finish()
    }

    override fun onRefresh() {

    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onTask(task: Task<*, *, *, *, *>) {}

    override fun <T> onItem(item: T) {

    }

    protected fun findMenuItemById(menuItemId: Int): MenuItem? = menu.findItem(menuItemId)

    protected fun getSearchMenuItem(): MenuItem? = findMenuItemById(searchMenuItemId())

    protected fun getSearchView(): SearchView? {
        val view = getSearchMenuItem()?.actionView ?: return null
        return view as SearchView
    }

    protected fun <T : ViewDataBinding> getBinding(): T {
        return binding as T
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
                .setMessage(
                    message
                        ?: if (messageRes != 0) getString(messageRes) else Constants.Default.STRING
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

    private fun initLayout(@LayoutRes layoutRes: Int) {
        if (fullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        if (hasBinding()) {
            binding = DataBindingUtil.setContentView(this, layoutRes)
            binding.setLifecycleOwner(this)
        } else {
            setContentView(layoutRes)
        }
    }

    private fun initToolbar() {
        val toolbarId = toolbarId()
        if (toolbarId != 0) {
            toolbar = findViewById<Toolbar>(toolbarId)
            setSupportActionBar(toolbar)
            if (homeUp()) {
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