package com.dreampany.tools.api.question.model

import com.dreampany.tools.api.question.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2020-02-13
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class TriviaQuestionsResponse (
    @SerializedName(value = Constants.Key.RESPONSE_CODE)
    val responseCode: Int,
    val results: List<TriviaQuestion>
) {
}