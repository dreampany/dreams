package com.dreampany.common.data.model

import com.dreampany.common.data.enums.BaseEnum
import com.dreampany.common.data.enums.UiState
import com.dreampany.common.misc.func.SmartError

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
sealed class Response<T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum, I> {

    data class UiResponse<T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        var uiState: UiState = UiState.DEFAULT
    )

    data class Progress<T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum, I>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        val progress: Boolean
    ) : Response<T, S, ST, A, I>()

    data class Error<T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum, I>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        val error: SmartError
    ) : Response<T, S, ST, A, I>()

    data class Result<T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum,
            R>(
        val type: T,
        val subtype: S,
        val state: ST,
        val action: A,
        val result: R?
    ) : Response<T, S, ST, A, R>()

    companion object {
        fun <T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum> response(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            uiState: UiState
        ): UiResponse<T, S, ST, A> = UiResponse(type, subtype, state, action, uiState)

        fun <T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum, I> response(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            progress: Boolean
        ): Response<T, S, ST, A, I> = Progress(type, subtype, state, action, progress)

        fun <T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum, I> response(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            error: SmartError
        ): Response<T, S, ST, A, I> =
            Error(type, subtype, state, action, error)

        fun <T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum, I> response(
            type: T,
            subtype: S,
            state: ST,
            action: A,
            result: I
        ): Response<T, S, ST, A, I> =
            Result(type, subtype, state, action, result)
    }
}