package com.dreampany.tools.data.model.news

import androidx.room.*
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.misc.constants.NewsConstants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 8/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.ID]
)
data class Article(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    @Embedded
    var source: Source? = Constants.Default.NULL,
    var author: String? = Constants.Default.NULL,
    var title: String = Constants.Default.STRING,
    var description: String? = Constants.Default.NULL,
    var content: String? = Constants.Default.NULL,
    var url: String = Constants.Default.STRING,
    var imageUrl: String? = Constants.Default.NULL,
    var publishedAt: Long = Constants.Default.LONG
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Article
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Article ($id) == $id"

    @Parcelize
    data class Source(
        @ColumnInfo(name = NewsConstants.Keys.News.SOURCE_ID)
        val id: String?,
        val name: String?
    ) : BaseParcel()
}