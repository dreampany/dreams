package com.dreampany.tools.data.model

import com.dreampany.framework.data.model.Base

/**
 * Created by roman on 2019-11-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Message : Base() {
    abstract var from: String
    abstract var to: String
}