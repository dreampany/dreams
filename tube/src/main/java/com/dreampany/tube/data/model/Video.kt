package com.dreampany.tube.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constants
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
        value = [Constants.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.ID]
)
data class Video(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var title: String? = Constants.Default.NULL,
    var description: String? = Constants.Default.NULL,
    var thumbnail: String? = Constants.Default.NULL,
    var channelId: String? = Constants.Default.NULL,
    var channelTitle: String? = Constants.Default.NULL,
    var categoryId: String? = Constants.Default.NULL,
    var tags: List<String>? = Constants.Default.NULL,
    var liveBroadcastContent: String? = Constants.Default.NULL,
    var duration: String? = Constants.Default.NULL,
    var dimension: String? = Constants.Default.NULL,
    var definition: String? = Constants.Default.NULL,
    var licensedContent: Boolean = Constants.Default.BOOLEAN,
    var viewCount: Long = Constants.Default.LONG,
    var likeCount: Long = Constants.Default.LONG,
    var dislikeCount: Long = Constants.Default.LONG,
    var favoriteCount: Long = Constants.Default.LONG,
    var commentCount: Long = Constants.Default.LONG,
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
        val item = other as Video
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Video ($id) == $id"

    @get:Ignore
    val isLive : Boolean
        get() = "live" == liveBroadcastContent

}