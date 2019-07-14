package com.dreampany.translate.data.model

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TextTranslationRequest(source: String, target: String, input: String) :
    TranslationRequest<String>(source, target, input) {
}