package com.dreampany.framework.data.model

/**
 * Created by Roman-372 on 8/2/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Alert : Base() {
    abstract var title: String
    abstract var description: String
}