package com.dreampany.tools.data.model.word

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
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
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Word.PART_OF_SPEECH)
    private var partOfSpeech: String? = Constants.Default.NULL,
    var text: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Word
        return Objects.equal(this.id, item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    @PropertyName(value = Constants.Keys.Word.PART_OF_SPEECH)
    fun setPartOfSpeech(partOfSpeech: String?) {
        this.partOfSpeech = partOfSpeech
    }

    @PropertyName(value = Constants.Keys.Word.PART_OF_SPEECH)
    fun getPartOfSpeech(): String? {
        return partOfSpeech
    }

/*    @Exclude
    fun getCleanText(): String? {
        return TextUtil.stripHtml(text)
    }*/

/*    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Definition
        return Objects.equal(item.partOfSpeech, partOfSpeech) && Objects.equal(item.text, text)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(partOfSpeech, text)
    }*/

}