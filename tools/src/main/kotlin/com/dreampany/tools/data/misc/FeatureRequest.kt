package com.dreampany.tools.data.misc

import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.model.Request
import com.dreampany.tools.data.enums.FeatureType
import com.dreampany.tools.data.model.Feature
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FeatureRequest(
    val type: FeatureType = FeatureType.DEFAULT,
    action: Action = Action.DEFAULT,
    input: Feature? = Constants.Default.NULL,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    favorite: Boolean = Constants.Default.BOOLEAN
) : Request<Feature>(
    action = action,
    input = input,
    important = important,
    progress = progress,
    favorite = favorite
) {
}