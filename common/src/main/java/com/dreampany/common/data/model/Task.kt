package com.dreampany.common.data.model

import com.dreampany.common.data.enums.BaseEnum
import com.dreampany.common.misc.constant.Constant

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class Task<T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I : BaseParcel>(
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