package com.dreampany.history.data.source.remote

import com.dreampany.history.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class WikiHistoryData(
    @SerializedName(Constants.History.EVENTS) val events: MutableList<WikiHistory>,
    @SerializedName(Constants.History.BIRTHS) val births: MutableList<WikiHistory>,
    @SerializedName(Constants.History.DEATHS) val deaths: MutableList<WikiHistory>
) {
}