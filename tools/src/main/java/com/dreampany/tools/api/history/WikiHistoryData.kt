package com.dreampany.tools.api.history

import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 9/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WikiHistoryData(
    @SerializedName(Constants.Keys.EVENTS) val events: List<WikiHistory>?,
    @SerializedName(Constants.Keys.BIRTHS) val births: List<WikiHistory>?,
    @SerializedName(Constants.Keys.DEATHS) val deaths: List<WikiHistory>?
)