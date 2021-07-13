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
sealed class Response<T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I> {

    data class UiResponse<T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum>(
        val type: T,
        val subtype: ST,
        val state: S,
        val action: A,
        var uiState: UiState = UiState.DEFAULT
    )

    data class Progress<T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I>(
        val type: T,
        val subtype: ST,
        val state: S,
        val action: A,
        val progress: Boolean
    ) : Response<T, ST, S, A, I>()

    data class Error<T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I>(
        val type: T,
        val subtype: ST,
        val state: S,
        val action: A,
        val error: SmartError
    ) : Response<T, ST, S, A, I>()

    data class Result<T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I>(
        val type: T,
        val subtype: ST,
        val state: S,
        val action: A,
        val result: I?
    ) : Response<T, ST, S, A, I>()

    companion object {
        fun <T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum> response(
            type: T,
            subtype: ST,
            state: S,
            action: A,
            uiState: UiState
        ): UiResponse<T, ST, S, A> = UiResponse(type, subtype, state, action, uiState)

        fun <T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I> response(
            type: T,
            subtype: ST,
            state: S,
            action: A,
            progress: Boolean
        ): Response<T, ST, S, A, I> = Progress(type, subtype, state, action, progress)

        fun <T : BaseEnum, S : BaseEnum, ST : BaseEnum, A : BaseEnum, I> response(
            type: T,
            subtype: ST,
            state: S,
            action: A,
            error: SmartError
        ): Response<T, ST, S, A, I> = Error(type, subtype, state, action, error)

        fun <T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I> response(
            type: T,
            subtype: ST,
            state: S,
            action: A,
            result: I
        ): Response<T, ST, S, A, I> = Result(type, subtype, state, action, result)
    }
}