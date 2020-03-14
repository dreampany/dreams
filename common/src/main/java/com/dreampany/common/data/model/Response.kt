package com.dreampany.common.data.model

import com.dreampany.common.data.enums.Action
import com.dreampany.common.data.enums.BaseType
import com.dreampany.common.data.enums.State
import com.dreampany.common.ui.enums.UiState

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
sealed class Response<T> {

    data class UiResponse(
        val type: BaseType,
        val subtype: BaseType,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        var uiState: UiState = UiState.DEFAULT
    )

    data class Progress<T>(
        val type: BaseType,
        val subtype: BaseType,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val loading: Boolean
    ) : Response<T>()

    data class Failure<T>(
        val type: BaseType,
        val subtype: BaseType,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val error: Throwable
    ) : Response<T>()

    data class Result<T>(
        val type: BaseType,
        val subtype: BaseType,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val data: T
    ) : Response<T>()

    data class Empty<T>(
        val type: BaseType,
        val subtype: BaseType,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val data: T?
    ) : Response<T>()

    companion object {
        fun response(
            type: BaseType,
            subtype: BaseType,
            state: State,
            action: Action,
            uiState: UiState
        ): UiResponse = UiResponse(type, subtype, state, action, uiState)

        fun <T> response(
            type: BaseType,
            subtype: BaseType,
            state: State,
            action: Action,
            loading: Boolean
        ): Response<T> =
            Progress(type, subtype, state, action, loading)

        fun <T> response(
            type: BaseType,
            subtype: BaseType,
            state: State,
            action: Action,
            error: Throwable
        ): Response<T> =
            Failure(type, subtype, state, action, error)

        fun <T> response(
            type: BaseType,
            subtype: BaseType,
            state: State,
            action: Action,
            data: T
        ): Response<T> =
            Result(type, subtype, state, action, data)

        fun <T> responseEmpty(
            type: BaseType,
            subtype: BaseType,
            state: State = State.DEFAULT,
            action: Action = Action.DEFAULT,
            data: T?
        ): Response<T> =
            Empty(type, subtype, state, action, data)
    }
}