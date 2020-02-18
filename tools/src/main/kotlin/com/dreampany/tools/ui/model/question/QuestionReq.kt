package com.dreampany.tools.ui.model.question

import com.dreampany.framework.data.enums.Difficult
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tools.data.model.question.Question
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-02-18
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class QuestionReq(
    var category: Question.Category? = null,
    var type: Question.Type? = null,
    var difficult: Difficult? = null,
    var limit: Long? = 0L
) : BaseParcel() {

    fun toJson() : String {
        return Gson().toJson(this)
    }

    companion object {
        fun parse(json: String) : QuestionReq {
           return Gson().fromJson(json, QuestionReq::class.java)
        }
    }
}