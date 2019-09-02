package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-10
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Word.ID],
        unique = true
    )],
    primaryKeys = [Constants.Word.ID]
)
data class Word(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var partOfSpeech: String? = Constants.Default.NULL,
    var pronunciation: String? = Constants.Default.NULL,
    var definitions: ArrayList<Definition>? = Constants.Default.NULL,
    var examples: ArrayList<String>? = Constants.Default.NULL,
    @Ignore
    var synonyms: ArrayList<String>? = Constants.Default.NULL,
    @Ignore
    var antonyms: ArrayList<String>? = Constants.Default.NULL,
    var categories: ArrayList<String>? = Constants.Default.NULL,
    var tags: ArrayList<String>? = Constants.Default.NULL,
    var notes: ArrayList<String>? = Constants.Default.NULL,
    var popularity: Int = Constants.Default.INT

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

    fun hasDefinitions(): Boolean {
        return definitions?.isEmpty() ?: false
    }

    fun hasExamples(): Boolean {
        return examples?.isEmpty() ?: false
    }

    fun hasSynonyms(): Boolean {
        return synonyms?.isEmpty() ?: false
    }

    fun hasAntonyms(): Boolean {
        return antonyms?.isEmpty() ?: false
    }

    fun hasCategories(): Boolean {
        return categories?.isEmpty() ?: false
    }

    fun hasTags(): Boolean {
        return tags?.isEmpty() ?: false
    }

    fun hasNotes(): Boolean {
        return notes?.isEmpty() ?: false
    }

    fun hasPartial(): Boolean {
        return if (DataUtil.isEmpty(partOfSpeech, pronunciation) && DataUtil.isEmpty(definitions) && DataUtil.isEmpty(examples)) false else true
    }

    fun hasFull(): Boolean {
        if (!hasPartial()) return false
        return !(DataUtil.isEmpty(synonyms) && DataUtil.isEmpty(antonyms))
    }
}