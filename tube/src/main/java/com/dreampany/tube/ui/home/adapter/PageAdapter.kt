package com.dreampany.tube.ui.home.adapter

import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.ui.adapter.BasePagerAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Page
import com.dreampany.tube.ui.home.fragment.VideosFragment
import com.dreampany.tube.ui.model.PageItem

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PageAdapter(fragment: Fragment) : BasePagerAdapter<Fragment>(fragment) {

    private val pages = arrayListOf<Page>()

    fun addItems(items: List<PageItem>) {
        pages.clear()

        items.forEach {
            pages.add(it.input)
            val video = UiTask(
                Type.PAGE,
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
                it.input.title.value,
                false
            )
        }
        notifyDataSetChanged()
    }

    fun hasUpdate(inputs: List<Page>): Boolean =
        (inputs.containsAll(pages) && pages.containsAll(inputs)).not()
}