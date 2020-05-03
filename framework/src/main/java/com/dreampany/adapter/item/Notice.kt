package com.dreampany.adapter.item

/**
 * Created by roman on 6/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class Notice(
    val from: Int = 0,
    val position: Int = 0,
    val operation: Int = 0
) {
    companion object {
        val ADD: Int = 1
        val CHANGE: Int = 2
        val REMOVE: Int = 3
        val MOVE: Int = 4
    }

    override fun toString(): String {
        return "Notification{" +
                "operation=" + operation +
                (if (operation == MOVE) ", fromPosition=$from" else "") +
                ", position=" + position +
                '}'
    }
}