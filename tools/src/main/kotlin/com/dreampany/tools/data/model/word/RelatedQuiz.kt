package com.dreampany.tools.data.model.word

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.Quiz.ID, Constants.Keys.Quiz.TYPE, Constants.Keys.Quiz.SUBTYPE, Constants.Keys.Quiz.LEVEL],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Quiz.ID, Constants.Keys.Quiz.TYPE, Constants.Keys.Quiz.SUBTYPE, Constants.Keys.Quiz.LEVEL]
)
data class RelatedQuiz(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var type: Type = Type.DEFAULT,
    var subtype: Subtype = Subtype.DEFAULT,
    var level: Level = Level.DEFAULT,
    var options: ArrayList<String>? = Constants.Default.NULL,
    var answer: String? = Constants.Default.NULL,
    var given: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Common.POINT_ID)
    var pointId: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, type, subtype, level)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as RelatedQuiz
        return Objects.equal(item.id, id) &&
                Objects.equal(item.type, type) &&
                Objects.equal(item.subtype, subtype) &&
                Objects.equal(item.level, level)
    }
}