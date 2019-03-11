package com.dreampany.lca.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.lca.ui.model.NewsItem
import eu.davidea.flexibleadapter.items.IFlexible


/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class NewsAdapter(listener: Any) : SmartAdapter<NewsItem>(listener) {

    private val publishedOnComparator: Comparator<IFlexible<*>>

    init {
        publishedOnComparator = PublishedOnComparator()
    }

    override fun addItems(items: List<NewsItem>): Boolean {
        for (item in items) {
            addItem(item, publishedOnComparator)
        }
        return true
    }

    class PublishedOnComparator : Comparator<IFlexible<*>> {
        override fun compare(p0: IFlexible<*>?, p1: IFlexible<*>?): Int {
            val left = p0 as NewsItem
            val right = p1 as NewsItem
            val leftItem = left.item
            val rightItem = right.item
            return (rightItem.publishedOn - leftItem.publishedOn).toInt()
        }
    }
}