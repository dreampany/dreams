package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.data.enums.ApkType
import com.dreampany.tools.data.enums.MediaType
import com.dreampany.tools.misc.Constants
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
        value = [Constants.Apk.ID],
        unique = true
    )],
    primaryKeys = [Constants.Apk.ID]
)
data class Apk(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING, // package
    override var mediaType: MediaType = MediaType.DEFAULT,
    override var name: String = Constants.Default.STRING,
    override var uri: String = Constants.Default.STRING,
    override var thumbUri: String? = Constants.Default.NULL,
    override var mimeType: String = Constants.Default.STRING,
    override var size: Long = Constants.Default.LONG,
    override var dateAdded: Long = Constants.Default.LONG,
    override var dateModified: Long = Constants.Default.LONG,
    var apkType: ApkType = ApkType.DEFAULT,
    var versionCode: Int = Constants.Default.INT,
    var versionName: String = Constants.Default.STRING
) : Media() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis(), mediaType = MediaType.APK) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id, mediaType = MediaType.APK) {

    }

    constructor(
        id: String = Constants.Default.STRING,
        name: String = Constants.Default.STRING,
        uri: String = Constants.Default.STRING,
        thumbUri: String? = Constants.Default.NULL,
        mimeType: String = Constants.Default.STRING,
        size: Long = Constants.Default.LONG,
        dateAdded: Long = Constants.Default.LONG,
        dateModified: Long = Constants.Default.LONG,
        apkType: ApkType = ApkType.DEFAULT,
        versionCode: Int = Constants.Default.INT,
        versionName: String = Constants.Default.STRING
    ) : this(
        time = TimeUtilKt.currentMillis(),
        id = id,
        mediaType = MediaType.APK,
        name = name,
        uri = uri,
        thumbUri = thumbUri,
        mimeType = mimeType,
        size = size,
        dateAdded = dateAdded,
        dateModified = dateModified,
        apkType = apkType,
        versionCode = versionCode,
        versionName = versionName
    ) {

    }
}