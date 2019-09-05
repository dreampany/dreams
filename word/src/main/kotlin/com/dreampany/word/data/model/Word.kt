package com.dreampany.word.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.DataUtil
import com.dreampany.framework.util.TimeUtil
import com.dreampany.word.misc.Constants
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Word.ID],
        unique = true
    )],
    primaryKeys = [Constants.Word.ID]
)
class Word(
    override var time: Long,
    override var id: String
) : Base() {

    @ColumnInfo(name = Constants.Word.PART_OF_SPEECH)
    private var partOfSpeech: String? = null
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

    @Ignore
    constructor() : this("") {

    }

    constructor(id: String) : this(TimeUtil.currentTime(), id) {

    }

    @Ignore
    private constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readString()!!) {
        partOfSpeech = parcel.readString()
        pronunciation = parcel.readString()
        if (parcel.readByte().compareTo(0x00) == 0) {
            definitions = null
        } else {
            definitions = mutableListOf()
            parcel.readList(definitions as MutableList<Any?>, Definition::class.java.classLoader)
        }
        examples = parcel.createStringArrayList()
        synonyms = parcel.createStringArrayList()
        antonyms = parcel.createStringArrayList()
        categories = parcel.createStringArrayList()
        tags = parcel.createStringArrayList()
        notes = parcel.createStringArrayList()
        popularity = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(time)
        dest.writeString(id)
        dest.writeString(partOfSpeech)
        dest.writeString(pronunciation)
        if (definitions == null) {
            dest.writeByte(0x00)
        } else {
            dest.writeByte(0x01)
            dest.writeList(definitions as MutableList<Any?>)
        }
        dest.writeStringList(examples)
        dest.writeStringList(synonyms)
        dest.writeStringList(antonyms)
        dest.writeStringList(categories)
        dest.writeStringList(tags)
        dest.writeStringList(notes)
        dest.writeInt(popularity)
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }
    }


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

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    fun setPartOfSpeech(partOfSpeech: String?) {
        this.partOfSpeech = partOfSpeech
    }

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    fun getPartOfSpeech(): String? {
        return partOfSpeech
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