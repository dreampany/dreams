package com.dreampany.tools.ui.news.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.framework.ui.adapter.BasePagerAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.news.NewsAction
import com.dreampany.tools.data.enums.news.NewsState
import com.dreampany.tools.data.enums.news.NewsSubtype
import com.dreampany.tools.data.enums.news.NewsType
import com.dreampany.tools.data.model.news.Article
import com.dreampany.tools.ui.news.fragment.ArticlesFragment

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticlePagerAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

    fun addItems() {
        val latest = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.LATEST,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            null as Article?
        )
        val world = UiTask(
            NewsType.DEFAULT,
            NewsSubtype.WORLD,
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
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                latest
            ),
            R.string.title_article_latest
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ArticlesFragment::class,
                world
            ),
            R.string.title_article_world
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
        notifyDataSetChanged()
    }
}