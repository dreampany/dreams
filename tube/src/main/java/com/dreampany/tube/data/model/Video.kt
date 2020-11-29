package com.dreampany.tube.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 30/6/20
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
data class Video(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var categoryId: String = Constant.Default.STRING,
    var title: String? = Constant.Default.NULL,
    var description: String? = Constant.Default.NULL,
    var thumbnail: String? = Constant.Default.NULL,
    var channelId: String? = Constant.Default.NULL,
    var channelTitle: String? = Constant.Default.NULL,
    var tags: List<String>? = Constant.Default.NULL,
    var liveBroadcastContent: String? = Constant.Default.NULL,
    var duration: String? = Constant.Default.NULL,
    var dimension: String? = Constant.Default.NULL,
    var definition: String? = Constant.Default.NULL,
    var licensedContent: Boolean = Constant.Default.BOOLEAN,
    var viewCount: Long = Constant.Default.LONG,
    var likeCount: Long = Constant.Default.LONG,
    var dislikeCount: Long = Constant.Default.LONG,
    var favoriteCount: Long = Constant.Default.LONG,
    var commentCount: Long = Constant.Default.LONG,
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
        val item = other as Video
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Video ($id) == $id"

    @get:Ignore
    val isLive : Boolean
        get() = "live" == liveBroadcastContent

}