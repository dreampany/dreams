package com.dreampany.framework.ui.model

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : Base>(
    val notify: Boolean = Constants.Default.BOOLEAN,
    val fullscreen: Boolean = Constants.Default.BOOLEAN,
    val collapseToolbar: Boolean = Constants.Default.BOOLEAN,
    val type: Type = Type.DEFAULT,
    val subtype: Subtype = Subtype.DEFAULT,
    val state: State = State.DEFAULT,
    val action: Action = Action.DEFAULT,
    override var id: String? = Constants.Default.NULL,
    override var input: T? = Constants.Default.NULL,
    override var extra: String? = Constants.Default.NULL
) : Task<T>(id, input, extra) {
}