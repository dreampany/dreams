package com.dreampany.tools.api.question.model

import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2020-02-13
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class TriviaQuestion(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @SerializedName("correct_answer")
    val answer: String,
    @SerializedName("incorrect_answers")
    val options: ArrayList<String>
) {
}