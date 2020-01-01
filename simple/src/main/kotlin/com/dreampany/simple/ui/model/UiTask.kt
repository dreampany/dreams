package com.dreampany.simple.ui.model

import com.dreampany.simple.data.enums.*
import com.dreampany.simple.data.model.Base
import com.dreampany.simple.data.model.Task
import com.dreampany.simple.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : Base>(
    var notify: Boolean = Constants.Default.BOOLEAN,
    var fullscreen: Boolean = Constants.Default.BOOLEAN,
    var collapseToolbar: Boolean = Constants.Default.BOOLEAN,
    var type: Type = Type.DEFAULT,
    var subtype: Subtype = Subtype.DEFAULT,
    var state: State = State.DEFAULT,
    var action: Action = Action.DEFAULT,
    override var id: String? = Constants.Default.NULL,
    override var input: T? = Constants.Default.NULL,
    override var extra: String? = Constants.Default.NULL
) : Task<T>(id, input, extra) {
}