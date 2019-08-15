package com.dreampany.frame.ui.model

import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.Task
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
    val type: Type = Type.DEFAULT,
    val subtype: Subtype = Subtype.DEFAULT,
    val state: State = State.DEFAULT,
    val action: Action = Action.DEFAULT,
    override var input: T? = null,
    override var extra: String? = null
) : Task<T>(input, extra) {
}