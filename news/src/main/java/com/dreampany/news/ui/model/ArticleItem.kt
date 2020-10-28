package com.dreampany.news.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.time
import com.dreampany.news.data.model.Article
import com.dreampany.news.databinding.ArticleItemBinding
import com.google.common.base.Objects
import com.dreampany.news.R
import com.dreampany.news.misc.setUrl
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticleItem
private constructor(
    val input: Article
) : ModelAbstractBindingItem<Article, ArticleItemBinding>(input) {

    companion object {
        fun getItem(
            input: Article
        ): ArticleItem = ArticleItem(input)
    }

    override fun hashCode(): Int = Objects.hashCode(input.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ArticleItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int = R.id.adapter_article_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        ArticleItemBinding.inflate(inflater, parent, false)


    override fun bindView(binding: ArticleItemBinding, payloads: List<Any>) {
        binding.icon.setUrl(input.imageUrl)
        binding.source.text = input.source?.name
        binding.title.text = input.title
        binding.publishedAt.text = input.publishedAt.time
    }

    override fun unbindView(binding: ArticleItemBinding) {
        binding.source.text = null
        binding.title.text = null
        binding.publishedAt.text = null
    }
}