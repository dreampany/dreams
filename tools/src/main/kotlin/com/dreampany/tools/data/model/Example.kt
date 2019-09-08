package com.dreampany.tools.data.model

import androidx.room.Ignore
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-09-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Example(
    var documentId: Long = Constants.Default.LONG,
    var exampleId: Long = Constants.Default.LONG,
    var author: String? = Constants.Default.NULL,
    var title: String? = Constants.Default.NULL,
    var text: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL,
    var year: Int? = Constants.Default.INT,
    var rating: Float? = Constants.Default.FLOAT
) : BaseParcel() {

    @Ignore
    constructor() : this(text = Constants.Default.NULL) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Example
        return Objects.equal(item.documentId, documentId) && Objects.equal(item.exampleId, exampleId)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(documentId, exampleId)
    }

}