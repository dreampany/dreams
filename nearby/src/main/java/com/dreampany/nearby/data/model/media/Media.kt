package com.dreampany.nearby.data.model.media

import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constants

/**
 * Created by roman on 27/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Media(
    var uri: String = Constants.Default.STRING,
    var thumbUri: String? = Constants.Default.NULL,
    var displayName: String? = Constants.Default.NULL,
    var title: String? = Constants.Default.NULL,
    var mimeType: String? = Constants.Default.NULL,
    var size: Long = Constants.Default.LONG,
    var dateAdded: Long = Constants.Default.LONG,
    var dateModified: Long = Constants.Default.LONG
) : Base()