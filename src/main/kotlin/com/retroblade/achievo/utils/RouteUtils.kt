package com.retroblade.achievo.utils

import com.retroblade.achievo.common.MethodResult
import com.retroblade.achievo.models.view.response.ErrorResponse
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

inline fun Route.getAutoResult(
    path: String,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(ApplicationCall) -> MethodResult<Any>
) {
    get(path) {
        val result = body(call)

        when (result) {
            is MethodResult.Success -> call.respond(
                HttpStatusCode.OK,
                result.data
            )
            is MethodResult.Error -> call.respond(
                result.httpCode,
                result.errorResponse
            )
        }
    }
}

inline fun <reified Model: Any> Route.postAutoResult(
    path: String,
    validator: Validator<Model>? = null,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(ApplicationCall, Model) -> MethodResult<Any>
) {
    post(path) {
        try {
            val model = call.receive<Model>()
            validator?.validate(model)
            call.request.queryParameters
            val result = body(call, model)

            when (result) {
                is MethodResult.Success -> call.respond(
                    HttpStatusCode.OK,
                    result.data
                )
                is MethodResult.Error -> call.respond(
                    result.httpCode,
                    result.errorResponse
                )
            }
        } catch (e: RequestValidationException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = e.message
                )
            )
        } catch(e: BadRequestException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "Something went wrong with your request"
                )
            )
        }
    }
}