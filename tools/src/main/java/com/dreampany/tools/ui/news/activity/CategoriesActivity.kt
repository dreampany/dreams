/*
package com.dreampany.tools.ui.news.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.misc.util.NotifyUtil
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.data.source.news.pref.NewsPref
import javax.inject.Inject
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.databinding.RecyclerActivityBinding
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.ui.news.adapter.FastCategoryAdapter
import com.dreampany.tools.ui.news.model.CategoryItem
import com.dreampany.tools.ui.news.vm.CategoryViewModel
import kotlinx.android.synthetic.main.content_recycler.view.*
import timber.log.Timber

*/
/**
 * Created by roman on 13/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 *//*

class CategoriesActivity : InjectActivity()  {

    @Inject
    internal lateinit var pref: NewsPref

    private lateinit var bind: RecyclerActivityBinding
    private lateinit var vm: CategoryViewModel
    private lateinit var adapter: FastCategoryAdapter

    override val layoutRes: Int = R.layout.recycler_activity
    override val menuRes: Int = R.menu.categories_menu
    override val toolbarId: Int = R.id.toolbar

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, "CategoriesActivity")

            params.put(Constant.Event.activity(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        vm.loadCategories()
        updateSubtitle()
    }

    override fun onStopUi() {
     }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done -> {
                onDonePressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onItemPressed(view: View, item: CategoryItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                adapter.toggle(item)
                updateSubtitle()
            }
            else -> {

            }
        }
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(CategoryViewModel::class)

        vm.subscribes(this, Observer { this.processResponses(it) })

        bind.swipe.isEnabled = false
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastCategoryAdapter(this::onItemPressed)
        adapter.initRecycler(state, bind.layoutRecycler.recycler)
    }

    private fun updateSubtitle() {
        val selection = adapter.selectionCount
        val total = adapter.itemCount
        val subtitle = getString(R.string.subtitle_categories, selection, total)
        setSubtitle(subtitle)

        val required = Constants.Count.News.MIN_PAGES - adapter.selectionCount
        val menuIconRes =
            if (required > 0) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_done_all_24
        findMenuItemById(R.id.action_done)?.setIcon(menuIconRes)
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<CategoryItem>>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<CategoryItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(result: List<CategoryItem>?) {
        if (result != null) {
            adapter.addItems(result)
            updateSubtitle()
        }
    }

    private fun onDonePressed() {
        val required = Constants.Count.News.MIN_PAGES - adapter.selectionCount
        if (required > 0) {
            NotifyUtil.shortToast(this, getString(R.string.notify_select_min_categories, required))
            return
        }
        pref.commitCategoriesSelection()
        val categories = adapter.selectedItems.map { it.input }
        pref.commitCategories(categories)

        val action = task?.action as Action?
        if (action == Action.BACK) {
            onBackPressed()
            return
        }
        open(NewsActivity::class, true)
    }
}*/
