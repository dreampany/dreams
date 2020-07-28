package com.dreampany.tube.ui.home.adapter

import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.ui.adapter.BasePagerFragmentAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.ui.home.fragment.VideosFragment
import com.dreampany.tube.ui.home.model.CategoryItem

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CategoryPagerAdapter(fragment: Fragment) : BasePagerFragmentAdapter<Fragment>(fragment) {

    private val categories = arrayListOf<Category>()

    fun addItems(items: List<CategoryItem>) {
        categories.clear()

        items.forEach {
            categories.add(it.input)
            val video = UiTask(
                Type.VIDEO,
                Subtype.DEFAULT,
                State.DEFAULT,
                Action.DEFAULT,
                it.input
            )
            addItem(
                com.dreampany.framework.misc.exts.createFragment(
                    VideosFragment::class,
                    video
                ),
                it.input.title.value(),
                false
            )
        }
        notifyDataSetChanged()
    }

    fun hasUpdate(inputs: List<Category>): Boolean =
        inputs.containsAll(categories) && categories.containsAll(inputs)
}