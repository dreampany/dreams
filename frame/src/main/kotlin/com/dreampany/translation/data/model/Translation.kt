package com.dreampany.translation.data.model

import com.dreampany.frame.data.model.BaseKt

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Translation(time: Long, id: String, var source: String, var target: String) :
    BaseKt(time, id) {

}