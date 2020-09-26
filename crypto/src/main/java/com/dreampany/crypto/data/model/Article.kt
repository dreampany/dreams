package com.dreampany.crypto.data.model

import androidx.room.*
import com.dreampany.crypto.misc.constants.AppConstants
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
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
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Article(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    @Embedded
    var source: Source? = Constant.Default.NULL,
    var author: String? = Constant.Default.NULL,
    var title: String = Constant.Default.STRING,
    var description: String? = Constant.Default.NULL,
    var content: String? = Constant.Default.NULL,
    var url: String = Constant.Default.STRING,
    var imageUrl: String? = Constant.Default.NULL,
    var publishedAt: Long = Constant.Default.LONG
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
        @ColumnInfo(name = AppConstants.Keys.News.SOURCE_ID)
        val id: String?,
        val name: String?
    ) : BaseParcel()
}