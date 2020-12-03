package com.dreampany.tube.ui.settings.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.misc.util.NotifyUtil
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tube.R
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.databinding.RecyclerActivityBinding
import com.dreampany.tube.misc.Constants
import com.dreampany.tube.ui.home.activity.HomeActivity
import com.dreampany.tube.ui.model.PageItem
import com.dreampany.tube.ui.settings.adapter.FastPageAdapter
import com.dreampany.tube.ui.vm.PageViewModel
import timber.log.Timber
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by roman on 9/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PagesActivity : InjectActivity() {

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: RecyclerActivityBinding
    private lateinit var vm: PageViewModel
    private lateinit var adapter: FastPageAdapter

    override val layoutRes: Int = R.layout.recycler_activity
    override val menuRes: Int = R.menu.pages_menu
    override val toolbarId: Int = R.id.toolbar

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, Constant.Param.screen(this))

            params.put(Constant.Event.key(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        vm.reads()
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

    private fun onItemPressed(view: View, item: PageItem) {
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
        bind = binding()
        vm = createVm(PageViewModel::class)

        //bind.fab.setImageResource(R.drawable.ic_photo_camera_black_48dp)
        /*bind.fab.visible()
        bind.fab.setOnSafeClickListener { openScanUi() }*/


        bind.swipe.isEnabled = false

        vm.subscribes(this, Observer { this.processResponses(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastPageAdapter(this::onItemPressed)
        adapter.initRecycler(state, bind.layoutRecycler.recycler)
    }

    private fun updateSubtitle() {
        val selection = adapter.selectionCount
        val total = adapter.itemCount
        val subtitle = getString(R.string.subtitle_categories, selection, total)
        setSubtitle(subtitle)

        val required = Constants.Count.MIN_CATEGORIES - adapter.selectionCount
        val menuIconRes =
            if (required > 0) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_done_all_24
        findMenuItemById(R.id.action_done)?.setIcon(menuIconRes)
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<PageItem>>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<PageItem>>) {
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

    private fun processResults(result: List<PageItem>?) {
        if (result != null) {
            adapter.addItems(result)
            updateSubtitle()
        }
    }

    private fun onDonePressed() {
        val required = Constants.Count.MIN_CATEGORIES - adapter.selectionCount
        if (required > 0) {
            NotifyUtil.shortToast(this, getString(R.string.notify_select_min_categories, required))
            return
        }
        pref.commitPagesSelection()
        val pages = adapter.selectedItems.map { it.input }
        pref.commitPages(pages)

        val action = task?.action as Action?
        if (action == Action.BACK) {
            onBackPressed()
            return
        }
        open(HomeActivity::class, true)
    }
}