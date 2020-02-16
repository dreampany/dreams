package com.dreampany.tools.data.model.word

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.DataUtil
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
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
        value = [Constants.Keys.Word.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Word.ID]
)
data class Word(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Word.PART_OF_SPEECH)
    private var partOfSpeech: String? = Constants.Default.NULL,
    var pronunciation: String? = Constants.Default.NULL,
    var definitions: ArrayList<Definition>? = Constants.Default.NULL,
    var examples: ArrayList<Example>? = Constants.Default.NULL,
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

    @PropertyName(value = Constants.Keys.Word.PART_OF_SPEECH)
    fun setPartOfSpeech(partOfSpeech: String?) {
        this.partOfSpeech = partOfSpeech
    }

    @PropertyName(value = Constants.Keys.Word.PART_OF_SPEECH)
    fun getPartOfSpeech(): String? {
        return partOfSpeech
    }

    fun hasDefinitions(): Boolean {
        return !definitions.isNullOrEmpty()
    }

    fun hasExamples(): Boolean {
        return examples?.isEmpty() ?: false
    }

    fun hasSynonyms(): Boolean {
        return !synonyms.isNullOrEmpty()
    }

    fun hasAntonyms(): Boolean {
        return !antonyms.isNullOrEmpty()
    }

    fun hasCategories(): Boolean {
        return !categories.isNullOrEmpty()
    }

    fun hasTags(): Boolean {
        return !tags.isNullOrEmpty()
    }

    fun hasNotes(): Boolean {
        return !notes.isNullOrEmpty()
    }

    @Exclude
    fun isEmpty(): Boolean {
        return DataUtil.isEmpty(partOfSpeech, pronunciation) &&
                DataUtil.isEmpty(definitions) &&
                DataUtil.isEmpty(examples) &&
                DataUtil.isEmpty(synonyms) &&
                DataUtil.isEmpty(antonyms)
    }

    fun hasPartial(): Boolean {
        return if (DataUtil.isEmpty(
                partOfSpeech,
                pronunciation
            ) && DataUtil.isEmpty(definitions) && DataUtil.isEmpty(examples)
        ) false else true
    }

    fun hasFull(): Boolean {
        if (!hasPartial()) return false
        return !(DataUtil.isEmpty(synonyms) && DataUtil.isEmpty(antonyms))
    }

    @Exclude
    fun weight(): Int {
        var weight = 0
        if (!partOfSpeech.isNullOrEmpty()) weight++
        if (!pronunciation.isNullOrEmpty()) weight++
        if (!definitions.isNullOrEmpty()) weight++
        if (!examples.isNullOrEmpty()) weight++
        if (!synonyms.isNullOrEmpty()) weight++
        if (!antonyms.isNullOrEmpty()) weight++
        return weight
    }

    @Exclude
    fun hasWeight(): Boolean {
        return weight() > 0
    }
}