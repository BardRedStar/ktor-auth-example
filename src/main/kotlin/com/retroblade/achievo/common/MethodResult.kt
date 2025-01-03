package com.retroblade.achievo.common

import com.retroblade.achievo.models.view.response.ErrorResponse
import io.ktor.http.*

sealed class MethodResult<out T> {
    data class Success<out T>(val data: T) : MethodResult<T>()
    data class Error(val httpCode: HttpStatusCode, val errorResponse: ErrorResponse) : MethodResult<Nothing>()

    companion object {

        inline fun <reified T> success(data: T): Success<T> = Success(data)

        fun error(httpCode: HttpStatusCode, message: String): Error = Error(httpCode, ErrorResponse(httpCode.value, message))
    }
}