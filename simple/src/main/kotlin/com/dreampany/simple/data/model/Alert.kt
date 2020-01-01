package com.dreampany.simple.data.model

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Alert : Base() {
    abstract var title: String
    abstract var description: String
}