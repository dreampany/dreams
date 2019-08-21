package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.frame.data.model.BaseParcel
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 7/22/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Definition(
    @ColumnInfo(name = Constants.Word.PART_OF_SPEECH)
    private var partOfSpeech: String? = Constants.Default.NULL,
    var text: String? = Constants.Default.NULL
) : BaseParcel() {

    @Ignore
    constructor() : this(partOfSpeech = Constants.Default.NULL) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Definition
        return Objects.equal(item.partOfSpeech, partOfSpeech) && Objects.equal(item.text, text)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(partOfSpeech, text)
    }

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    fun setPartOfSpeech(partOfSpeech: String?) {
        this.partOfSpeech = partOfSpeech
    }

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    fun getPartOfSpeech(): String? {
        return partOfSpeech
    }
}