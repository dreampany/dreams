package com.dreampany.tools.ui.news.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.ui.adapter.BasePagerFragmentAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.news.Page
import com.dreampany.tools.ui.news.fragment.ArticlesFragment
import com.dreampany.tools.ui.news.model.PageItem

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PageAdapter(activity: AppCompatActivity) : BasePagerFragmentAdapter<Fragment>(activity) {

    private val pages = arrayListOf<Page>()

    fun addItems(items: List<PageItem>) {
        pages.clear()

        items.forEach {
            pages.add(it.input)
            val task = UiTask(
                Type.PAGE,
                Subtype.DEFAULT,
                State.DEFAULT,
                Action.DEFAULT,
                it.input
            )
            addItem(
                com.dreampany.framework.misc.exts.createFragment(
                    ArticlesFragment::class,
                    task
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