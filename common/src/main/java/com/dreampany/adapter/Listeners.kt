package com.dreampany.adapter

import android.view.View

/**
 * Created by roman on 11/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface OnItemClickListener {
    fun onItemClick(view: View, position: Int): Boolean
}

interface OnItemLongClickListener {
    fun onItemLongClick(view: View, position: Int)
}