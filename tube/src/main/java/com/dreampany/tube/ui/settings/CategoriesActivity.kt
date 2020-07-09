package com.dreampany.tube.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tube.R
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.databinding.RecyclerActivityBinding
import com.dreampany.tube.ui.home.model.CategoryItem
import com.dreampany.tube.ui.home.vm.CategoryViewModel
import com.dreampany.tube.ui.settings.adapter.FastCategoryAdapter
import timber.log.Timber

/**
 * Created by roman on 9/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CategoriesActivity : InjectActivity() {

    private lateinit var bind: RecyclerActivityBinding
    private lateinit var vm: CategoryViewModel
    private lateinit var adapter: FastCategoryAdapter

    override val layoutRes: Int
        get() = R.layout.recycler_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        //initRecycler(state)
        vm.loadCategories()
    }

    override fun onStopUi() {
    }

    private fun onItemPressed(view: View, item: CategoryItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                //openCoinUi(item)
            }
            else -> {

            }
        }
    }

    private fun initUi() {
        if (!::bind.isInitialized) {
            bind = getBinding()
            //bind.fab.setImageResource(R.drawable.ic_photo_camera_black_48dp)
            /*bind.fab.visible()
            bind.fab.setOnSafeClickListener { openScanUi() }*/
            vm = createVm(CategoryViewModel::class)
            vm.subscribes(this, Observer { this.processResponses(it) })
        }
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastCategoryAdapter(
                this::onItemPressed
            )
            adapter.initRecycler(
                state,
                bind.layoutRecycler.recycler
            )
        }
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
        }
    }
}