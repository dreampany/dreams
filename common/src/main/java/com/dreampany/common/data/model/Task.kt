package com.dreampany.common.data.model

import com.dreampany.common.data.enums.Action
import com.dreampany.common.data.enums.BaseType
import com.dreampany.common.data.enums.State
import com.dreampany.common.misc.constant.Constants

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Task<T : BaseParcel, X : BaseType, Y : BaseType>(
    open var notify: Boolean = Constants.Default.BOOLEAN,
    open var fullscreen: Boolean = Constants.Default.BOOLEAN,
    open var collapseToolbar: Boolean = Constants.Default.BOOLEAN,
    open var type: X,
    open var subtype: Y,
    open var state: State = State.DEFAULT,
    open var action: Action = Action.DEFAULT,
    open var id: String? = Constants.Default.NULL,
    open var ids: List<String>? = Constants.Default.NULL,
    open var input: T? = Constants.Default.NULL,
    open var inputs: List<T>? = Constants.Default.NULL,
    open var extra: String? = Constants.Default.NULL,
    open var extras: List<String>? = Constants.Default.NULL
) : BaseParcel()