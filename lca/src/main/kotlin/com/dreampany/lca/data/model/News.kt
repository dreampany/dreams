package com.dreampany.lca.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.lca.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 8/2/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.News.ID],
        unique = true
    )],
    primaryKeys = [Constants.News.ID]
)
data class News(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var newsId: Long =  Constants.Default.LONG,
    var guid: String? = Constants.Default.NULL,
    var publishedOn: Long =  Constants.Default.LONG,
    var imageUrl: String? = Constants.Default.NULL,
    var title: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL,
    var source: String? = Constants.Default.NULL,
    var body: String? = Constants.Default.NULL,
    var tags: String? = Constants.Default.NULL,
    var categories: String? = Constants.Default.NULL,
    var upVotes: Long = Constants.Default.LONG,
    var downVotes: Long = Constants.Default.LONG,
    var language: String? = Constants.Default.NULL,
    @Ignore
    var sourceInfo: SourceInfo? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {
    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {}
}