package com.dreampany.word.data.model

import com.dreampany.framework.data.model.BaseKt
import com.dreampany.framework.util.TimeUtil
import com.dreampany.word.ui.enums.MoreType
import kotlinx.android.parcel.Parcelize


/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Parcelize
data class More(
    override var time: Long,
    override var id: String,
    var type : MoreType
) : BaseKt() {

    constructor(type: MoreType) : this(TimeUtil.currentTime(), type.name, type) {
        this.type = type
    }
}