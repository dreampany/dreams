package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.enums.AppType
import com.dreampany.tools.data.enums.MediaType
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.App.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.App.ID]
)
data class App(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING, // package
    override var mediaType: MediaType = MediaType.DEFAULT,
    override var name: String? = Constants.Default.NULL,
    override var uri: String? = Constants.Default.NULL,
    override var thumbUri: String? = Constants.Default.NULL,
    override var mimeType: String? = Constants.Default.NULL,
    override var size: Long = Constants.Default.LONG,
    override var dateAdded: Long = Constants.Default.LONG,
    override var dateModified: Long = Constants.Default.LONG,
    var appType: AppType = AppType.DEFAULT,
    var versionCode: Int = Constants.Default.INT,
    var versionName: String? = Constants.Default.NULL
) : Media() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis(), mediaType = MediaType.APP) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id, mediaType = MediaType.APP) {

    }

    constructor(
        id: String = Constants.Default.STRING,
        name: String? = Constants.Default.NULL,
        uri: String? = Constants.Default.NULL,
        thumbUri: String? = Constants.Default.NULL,
        mimeType: String? = Constants.Default.NULL,
        size: Long = Constants.Default.LONG,
        dateAdded: Long = Constants.Default.LONG,
        dateModified: Long = Constants.Default.LONG,
        appType: AppType = AppType.DEFAULT,
        versionCode: Int = Constants.Default.INT,
        versionName: String = Constants.Default.STRING
    ) : this(
        time = TimeUtilKt.currentMillis(),
        id = id,
        mediaType = MediaType.APP,
        name = name,
        uri = uri,
        thumbUri = thumbUri,
        mimeType = mimeType,
        size = size,
        dateAdded = dateAdded,
        dateModified = dateModified,
        appType = appType,
        versionCode = versionCode,
        versionName = versionName
    )

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as App
        return Objects.equal(this.id, item.id)
    }
}