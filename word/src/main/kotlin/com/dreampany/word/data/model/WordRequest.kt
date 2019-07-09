package com.dreampany.word.data.model

import com.dreampany.frame.data.model.Request

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordRequest : Request<Word>() {
    var inputWord: String? = null
    var source: String? = null
    var target: String? = null
    var translate: Boolean = false
    var recentWord: Boolean = false
}