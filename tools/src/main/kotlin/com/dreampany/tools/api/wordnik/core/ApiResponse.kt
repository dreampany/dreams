package com.dreampany.tools.api.wordnik.core

/**
 * Created by roman on 2019-06-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

enum class ResponseType {
    Success, Informational, Redirection, ClientError, ServerError
}

abstract class ApiResponse<T>(val responseType: ResponseType) {
    abstract val statusCode: Int
    abstract val headers: Map<String, List<String>>
}

class Success<T>(
    val data: T,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf()
) : ApiResponse<T>(ResponseType.Success)

class Informational<T>(
    val statusText: String,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf()
) : ApiResponse<T>(ResponseType.Informational)

class Redirection<T>(
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf()
) : ApiResponse<T>(ResponseType.Redirection)

class ClientError<T>(
    val body: Any? = null,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf()
) : ApiResponse<T>(ResponseType.ClientError)

class ServerError<T>(
    val message: String? = null,
    val body: Any? = null,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>>
) : ApiResponse<T>(ResponseType.ServerError)