package com.dreampany.tools.data.model

import com.dreampany.frame.data.model.Base
import com.dreampany.tools.data.enums.MediaType

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Media : Base() {
    abstract var mediaType: MediaType
    abstract var name: String?
    abstract var uri: String?
    abstract var thumbUri: String?
    abstract var mimeType: String?
    abstract var size: Long
    abstract var dateAdded: Long
    abstract var dateModified: Long
}