package com.dreampany.framework.data.model

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.misc.constant.Constant

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
    open var input: I? = Constant.Default.NULL,
    open var inputs: List<I>? = Constant.Default.NULL,
    open var id: String? = Constant.Default.NULL,
    open var ids: List<String>? = Constant.Default.NULL,
    open var extra: String? = Constant.Default.NULL,
    open var extras: List<String>? = Constant.Default.NULL,
    open var url: String? = Constant.Default.NULL,
    open var notify: Boolean = Constant.Default.BOOLEAN,
    open var fullscreen: Boolean = Constant.Default.BOOLEAN,
    open var collapseToolbar: Boolean = Constant.Default.BOOLEAN
) : BaseParcel()