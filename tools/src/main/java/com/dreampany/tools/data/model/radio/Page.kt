package com.dreampany.tools.data.model.radio

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.enums.BaseType
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.framework.misc.exts.title
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 8/11/20
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
data class Page(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var type: Type = Type.DEFAULT,
    var title: String = Constant.Default.STRING
) : Base() {

    @Ignore
    constructor() : this(time = currentMillis) {

    }

    constructor(id: String) : this(time = currentMillis, id = id) {

    }

    constructor(type: Page.Type) : this(time = currentMillis, id = type.value, type = type, title = type.value.title) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Page
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Page ($id) == $id"

    @Parcelize
    enum class Type : BaseType {
        DEFAULT,
        LOCAL,
        /*COUNTRY,
        LANGUAGE,*/
        TREND,
        POPULAR,
        RECENT,
        CHANGE,
        CUSTOM;

        override val value: String get() = name

        val isLocal : Boolean
            get() = this == LOCAL

        val isCustom : Boolean
            get() = this == CUSTOM
    }
}