package com.dreampany.cast.data.model

import com.dreampany.cast.ui.enums.MoreType
import com.dreampany.frame.data.model.BaseKt
import kotlinx.android.parcel.Parcelize


/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Parcelize
data class More(
    override val id: String,
    override val time: Long,
    val type: MoreType) : BaseKt() {
}