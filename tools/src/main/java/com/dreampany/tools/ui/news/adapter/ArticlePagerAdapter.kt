package com.dreampany.tools.ui.news.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.exts.toTitle
import com.dreampany.framework.ui.adapter.BasePagerFragmentAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.news.Category
import com.dreampany.tools.ui.news.fragment.ArticlesFragment
import com.dreampany.tools.ui.news.model.CategoryItem

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticlePagerAdapter(activity: AppCompatActivity) : BasePagerFragmentAdapter<Fragment>(activity) {

    private val categories = arrayListOf<Category>()

    fun addItems(items: List<CategoryItem>) {
        categories.clear()

        items.forEach {
            categories.add(it.input)
            val task = UiTask(
                Type.CATEGORY,
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
                it.input.category.toTitle(),
                false
            )
        }
        notifyDataSetChanged()
    }

    fun hasUpdate(inputs: List<Category>): Boolean =
        (inputs.containsAll(categories) && categories.containsAll(inputs)).not()

    /*fun addItems() {
        val country = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.COUNTRY,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        val general = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.GENERAL,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        val health = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.HEALTH,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        val business = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.BUSINESS,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        val entertainment = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.ENTERTAINMENT,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        val sports = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.SPORTS,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        val science = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.SCIENCE,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        val technology = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.TECHNOLOGY,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                country
            ),
            activity.country
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                general
            ),
            R.string.title_article_general
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                health
            ),
            R.string.title_article_health
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                business
            ),
            R.string.title_article_business
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                entertainment
            ),
            R.string.title_article_entertainment
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                sports
            ),
            R.string.title_article_sports
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                science
            ),
            R.string.title_article_science
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                technology
            ),
            R.string.title_article_technology
        )
        notifyDataSetChanged()
    }*/
}