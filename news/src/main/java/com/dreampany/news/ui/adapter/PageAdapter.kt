package com.dreampany.news.ui.adapter

import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.ui.adapter.BasePagerFragmentAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.news.data.enums.Action
import com.dreampany.news.data.enums.State
import com.dreampany.news.data.enums.Subtype
import com.dreampany.news.data.enums.Type
import com.dreampany.news.data.model.Page
import com.dreampany.news.ui.fragment.ArticlesFragment
import com.dreampany.news.ui.model.PageItem

/**
 * Created by roman on 29/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PageAdapter(fragment: Fragment) : BasePagerFragmentAdapter<Fragment>(fragment) {

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