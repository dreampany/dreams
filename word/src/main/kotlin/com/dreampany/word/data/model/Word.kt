package com.dreampany.word.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.util.DataUtil
import com.dreampany.word.misc.Constants
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 7/17/2019
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
    override var id: String,
    override var time: Long
) : BaseKt() {

    @Ignore
    constructor() : this("") {

    }

    constructor(id: String) : this(id, 0L) {

    }

    @ColumnInfo(name = Constants.Word.PART_OF_SPEECH)
    @PropertyName(Constants.Word.PART_OF_SPEECH)
    var partOfSpeech: String? = null
    var pronunciation: String? = null
    var definitions: MutableList<Definition>? = null
    var examples: MutableList<String>? = null
    @Ignore
    var synonyms: MutableList<String>? = null
    @Ignore
    var antonyms: MutableList<String>? = null
    var categories: MutableList<String>? = null
    var tags: MutableList<String>? = null
    var notes: MutableList<String>? = null
    var popularity: Int = 0

    override fun toString(): String {
        return "Word ($id) == $id"
    }

    fun copyWord(from: Word) {
        id = from.id
        partOfSpeech = from.partOfSpeech
        pronunciation = from.pronunciation
        definitions = from.definitions
        examples = from.examples
        synonyms = from.synonyms
        antonyms = from.antonyms
        categories = from.categories
        tags = from.tags
        notes = from.notes
        popularity = from.popularity
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
        return if (DataUtil.isEmpty(
                partOfSpeech,
                pronunciation
            ) && DataUtil.isEmpty(definitions) && DataUtil.isEmpty(examples)
        ) {
            false
        } else true
    }

    fun hasFull(): Boolean {
        if (!hasPartial()) {
            return false
        }
        return if (DataUtil.isEmpty(synonyms) && DataUtil.isEmpty(antonyms)) {
            false
        } else true
    }
}