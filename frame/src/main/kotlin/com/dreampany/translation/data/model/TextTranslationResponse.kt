package com.dreampany.translation.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TextTranslationResponse {
    @SerializedName("text")
    @Expose
    val text: MutableList<String>? = null
}