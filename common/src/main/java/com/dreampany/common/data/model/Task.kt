package com.dreampany.common.data.model

import com.dreampany.common.data.enums.*
import com.dreampany.common.misc.constant.Constants

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Task<
        T : BaseType,
        S : BaseSubtype,
        ST : BaseState,
        A : BaseAction,
        I : BaseParcel>(
    open var type: T,
    open var subtype: S,
    open var state: ST,
    open var action: A,
    open var input: I? = Constants.Default.NULL,
    open var inputs: List<I>? = Constants.Default.NULL,
    open var id: String? = Constants.Default.NULL,
    open var ids: List<String>? = Constants.Default.NULL,
    open var extra: String? = Constants.Default.NULL,
    open var extras: List<String>? = Constants.Default.NULL,
    open var notify: Boolean = Constants.Default.BOOLEAN,
    open var fullscreen: Boolean = Constants.Default.BOOLEAN,
    open var collapseToolbar: Boolean = Constants.Default.BOOLEAN
) : BaseParcel()