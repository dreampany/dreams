package com.dreampany.translation.data.model

import androidx.room.Entity
import androidx.room.Index
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.translation.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Entity(
    indices = [Index(
        value = [Constants.Translation.INPUT, Constants.Translation.SOURCE, Constants.Translation.TARGET],
        unique = true
    )],
    primaryKeys = [Constants.Translation.INPUT, Constants.Translation.SOURCE, Constants.Translation.TARGET]
)
@IgnoreExtraProperties
@Parcelize
data class TextTranslation(
    override var id: String?,
    override var time: Long,
    val input: String,
    val source: String,
    val target: String,
    val output: String
) : BaseKt() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as TextTranslation
        return Objects.equal(item.input, input)
                && Objects.equal(item.source, source)
                && Objects.equal(item.target, target)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(input, source, target)
    }
}