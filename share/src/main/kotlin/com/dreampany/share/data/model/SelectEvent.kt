package com.dreampany.share.data.model

import com.dreampany.media.data.enums.MediaType


/**
 * Created by Hawladar Roman on 7/19/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
data class SelectEvent(val type: MediaType, val selected: Int, val total: Int) {
}