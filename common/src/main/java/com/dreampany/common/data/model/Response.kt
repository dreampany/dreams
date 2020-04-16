package com.dreampany.common.data.model

import com.dreampany.common.data.enums.*
import com.dreampany.common.ui.enums.UiState

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
sealed class Response<
        T : BaseType,
        S : BaseSubtype,
        ST : BaseState,
        A : BaseAction,
        OUT> {

    data class UiResponse<
            T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        var uiState: UiState = UiState.DEFAULT
    )

    data class Progress<T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            OUT>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        val progress: Boolean
    ) : Response<T, S, ST, A, OUT>()

    data class Error<T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            OUT>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        val error: Throwable
    ) : Response<T, S, ST, A, OUT>()

    data class Result<T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            OUT>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        val result: OUT
    ) : Response<T, S, ST, A, OUT>()

    data class Empty<T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            OUT>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        val result: OUT?
    ) : Response<T, S, ST, A, OUT>()

    companion object {
        fun <T : BaseType,
                S : BaseSubtype,
                ST : BaseState,
                A : BaseAction> response(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            uiState: UiState
        ): UiResponse<T, S, ST, A> = UiResponse(type, subtype, state, action, uiState)

        fun <T : BaseType,
                S : BaseSubtype,
                ST : BaseState,
                A : BaseAction,
                OUT> response(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            progress: Boolean
        ): Response<T, S, ST, A, OUT> = Progress(type, subtype, state, action, progress)

        fun <T : BaseType,
                S : BaseSubtype,
                ST : BaseState,
                A : BaseAction,
                OUT> response(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            error: Throwable
        ): Response<T, S, ST, A, OUT> =
            Error(type, subtype, state, action, error)

        fun <T : BaseType,
                S : BaseSubtype,
                ST : BaseState,
                A : BaseAction,
                OUT> response(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            result: OUT
        ): Response<T, S, ST, A, OUT> =
            Result(type, subtype, state, action, result)

        fun <T : BaseType,
                S : BaseSubtype,
                ST : BaseState,
                A : BaseAction,
                OUT> responseEmpty(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            result: OUT?
        ): Response<T, S, ST, A, OUT> =
            Empty(type, subtype, state, action, result)
    }
}