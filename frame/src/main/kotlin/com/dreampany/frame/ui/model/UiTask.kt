package com.dreampany.frame.ui.model

import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : Base>(
    val notify:Boolean = Constants.Default.BOOLEAN,
    val fullscreen: Boolean =  Constants.Default.BOOLEAN,
    val type: Type = Type.DEFAULT,
    val subtype: Subtype = Subtype.DEFAULT,
    val state: State = State.DEFAULT,
    val action: Action = Action.DEFAULT,
    override var input: T? = Constants.Default.NULL,
    override var extra: String? = Constants.Default.NULL
) : Task<T>(input, extra) {
}