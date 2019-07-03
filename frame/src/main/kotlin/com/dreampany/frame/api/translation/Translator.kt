package com.dreampany.frame.api.translation

import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 9/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
class Translator @Inject constructor() {
    private var baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/"
}