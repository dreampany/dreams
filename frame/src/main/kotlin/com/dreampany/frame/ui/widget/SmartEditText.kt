package com.dreampany.frame.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText


/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SmartEditText : EditText {

    interface OnCutCopyPasteListener {
        fun onCut()
        fun onCopy()
        fun onPaste()
    }

    private var listener: OnCutCopyPasteListener? = null

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {

    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        val consumed = super.onTextContextMenuItem(id)
        when (id) {
            android.R.id.cut -> listener?.onCut()
            android.R.id.copy -> listener?.onCopy()
            android.R.id.paste -> listener?.onPaste()
        }
        return consumed
    }

    fun setOnCutCopyPasteListener(listener: OnCutCopyPasteListener) {
        this.listener = listener
    }
}