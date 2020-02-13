package com.dreampany.tools.data.model.resume

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.Resume.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Resume.ID]
)
data class Resume(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    @Embedded
    var profile: Profile? = Constants.Default.NULL,
    var skills: ArrayList<Skill>? = Constants.Default.NULL,
    var experiences: ArrayList<Experience>? = Constants.Default.NULL,
    var projects: ArrayList<Project>? = Constants.Default.NULL,
    var schools: ArrayList<School>? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Resume
        return Objects.equal(this.id, item.id)
    }
}