package com.dreampany.translate.data.model

import com.dreampany.frame.data.model.Request

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class TranslationRequest<T>(val source: String, val target: String, input: T) :
    Request<T>(input) {

}