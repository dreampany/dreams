package com.dreampany.tools.data.model.question

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.enums.Difficult
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-02-13
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModifiedØ
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Keys.Question.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Question.ID]
)
data class Question(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var category: Category? = Constants.Default.NULL,
    var type: Type? = Constants.Default.NULL,
    var difficult: Difficult? = Constants.Default.NULL,
    var question: String? = Constants.Default.NULL,
    var answer: String? = Constants.Default.NULL,
    var options: ArrayList<String>? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Question
        return Objects.equal(this.id, item.id)
    }

    @Parcelize
    enum class Category(val code: Int) : Parcelable {
        GENERAL_KNOWLEDGE(9),
        BOOK(10),
        FILM(11),
        MUSIC(12),
        MUSICAL_THEATRE(13),
        TELEVISION(14),
        VIDEO_GAME(15),
        BOARD_GAME(16),
        SCIENCE_NATURE(17),
        COMPUTER(18),
        MATHEMATICS(19),
        MYTHOLOGY(20),
        SPORTS(21),
        GEOGRAPHY(23),
        HISTORY(24),
        POLITICS(25),
        ART(26),
        CELEBRITIES(27),
        ANIMALS(28),
        VEHICLES(29),
        COMICS(30),
        GADGETS(31),
        ANIME_MANGA(32),
        CARTOON_ANIMATION(33)
    }

    @Parcelize
    enum class Type(val code: String) : Parcelable {
        TRUE_FALSE(Constants.Values.QuestionType.TRUE_FALSE),
        MULTIPLE(Constants.Values.QuestionType.MULTIPLE)
    }
}