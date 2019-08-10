package com.dreampany.tools.ui.model

import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.Task
import com.dreampany.tools.ui.enums.UiAction
import com.dreampany.tools.ui.enums.UiSubtype
import com.dreampany.tools.ui.enums.UiType
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : Base>(
    val fullscreen: Boolean =  false,
    val type: UiType = UiType.DEFAULT,
    val subtype: UiSubtype = UiSubtype.DEFAULT,
    val action: UiAction = UiAction.DEFAULT,
    override var input: T? = null,
    override var comment: String? = null
) : Task<T>(input, comment) {
}